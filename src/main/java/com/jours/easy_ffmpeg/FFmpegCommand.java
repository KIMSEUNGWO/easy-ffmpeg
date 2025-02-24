package com.jours.easy_ffmpeg;

import com.jours.easy_ffmpeg.config.HlsConvertConfig;
import com.jours.easy_ffmpeg.stream.AudioStream;
import com.jours.easy_ffmpeg.stream.FFprobeResult;
import com.jours.easy_ffmpeg.stream.VideoStream;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FFmpegCommand {

    private final HlsConvertConfig config;

    private final String ffmpegPath;

    private final Path storePath;
    private final Path videoPath;

    private final VideoStream videoStream;
    private final AudioStream audioStream;

    private FFmpegCommand(HlsConvertConfig config, String ffmpegPath, Path storePath, Path videoPath, VideoStream videoStream, AudioStream audioStream) {
        this.config = config;
        this.ffmpegPath = ffmpegPath;
        this.storePath = storePath;
        this.videoPath = videoPath;
        this.videoStream = videoStream;
        this.audioStream = audioStream;
    }

    protected static FFmpegCommand newCommand(HlsConvertConfig config, String ffmpegPath, Path videoPath, Path originalVideoPath, FFprobeResult probeResult) {
        return new FFmpegCommand(config, ffmpegPath, videoPath, originalVideoPath, probeResult.toVideoStream(), probeResult.toAudioStream());
    }


    protected List<String> buildCommand() {
        List<String> command = new ArrayList<>();
        command.addAll(List.of(ffmpegPath, "-i", videoPath.toString()));
        command.addAll(List.of("-filter_complex", buildFilterComplex()));
        command.addAll(buildMaps());
        command.addAll(List.of("-c:v", "libx264"));
        if (audioStream.exists()) {
            command.addAll(List.of("-c:a", "aac"));
        }
        command.addAll(buildBitrates());
        command.addAll(buildHlsOptions());

        return command;
    }


    private List<String> buildHlsOptions() {
        List<String> options = new ArrayList<>();

        // 기본 HLS 옵션
        options.addAll(List.of(
            "-var_stream_map", buildVarStreamMap(),
            "-f", "hls",
            "-hls_time", String.valueOf(config.getHlsTime()),
            "-hls_list_size", String.valueOf(config.getListSize()),
            "-hls_segment_type", config.getSegmentType()
        ));

        // 키프레임 간격 설정
        if (config.getKeyFrameInterval() > 0) {
            options.addAll(List.of("-g", String.valueOf(config.getKeyFrameInterval())));
        }

        // 암호화 설정
        if (config.isEncryption() && config.getKeyInfoFile() != null) {
            options.addAll(List.of("-hls_key_info_file", config.getKeyInfoFile()));
        }

        // 재생목록 타입 설정
        if (config.getPlaylistType() != null) {
            options.addAll(List.of("-hls_playlist_type", config.getPlaylistType()));
        }

        // 세그먼트 시작 번호 설정
        if (config.getStartNumber() > 0) {
            options.addAll(List.of("-start_number", String.valueOf(config.getStartNumber())));
        }

        // 세그먼트 파일명 및 재생목록 설정
        String segmentFormat = String.format("%s_%%0%dd.ts", config.getSegmentName(), config.getSegmentDigits());
        options.addAll(List.of(
            "-hls_segment_filename",
            String.format("%s/%s_%%v/%s", storePath, config.getStreamName(), segmentFormat),
            "-master_pl_name",
            String.format("%s.m3u8", config.getMasterName()),
            String.format("%s/%s_%%v/playlist.m3u8", storePath, config.getStreamName())
        ));

        // 불연속성 설정
        if (config.isDiscontinuity()) {
            options.add("-hls_flags");
            options.add("discontinuity");
        }

        return options;
    }


    private String buildFilterComplex() {
        List<String> scales = videoStream.getScales();

        String splitCount = String.format("[0:v]split=%d", scales.size());
        String outputs = scales.stream()
            .map(s -> String.format("[v%d]", scales.indexOf(s) + 1))
            .collect(Collectors.joining(""));

        return splitCount + outputs + ";" + String.join(";", scales);
    }

    private List<String> buildBitrates() {
        List<String> bitrates = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (int i = 0; i < qualities.size(); i++) {
            bitrates.add("-b:v:" + i);
            bitrates.add(VideoStream.BITRATE.get(qualities.get(i)));
        }

        return bitrates;
    }

    private String buildVarStreamMap() {
        List<String> streamMaps = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (int i = 0; i < qualities.size(); i++) {
            String quality = qualities.get(i);
            streamMaps.add(audioStream.exists()
                ? String.format("v:%d,a:%d,name:%sp", i, i, quality)
                : String.format("v:%d,name:%sp", i, quality));
        }

        return String.join(" ", streamMaps);
    }

    private List<String> buildMaps() {
        List<String> maps = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (String quality : qualities) {
            maps.add("-map");
            maps.add(String.format("[v%sp]", quality));
            if (audioStream.exists()) {
                maps.add("-map"); maps.add("0:a");
            }
        }
        return maps;
    }
}
