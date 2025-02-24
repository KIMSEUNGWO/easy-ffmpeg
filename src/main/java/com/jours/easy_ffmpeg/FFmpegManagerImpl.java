package com.jours.easy_ffmpeg;

import com.jours.easy_ffmpeg.config.FFmpegContextConfiguration;
import com.jours.easy_ffmpeg.config.PropertyManager;
import com.jours.easy_ffmpeg.stream.FFprobeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
class FFmpegManagerImpl extends FFmpegManager {

    private final PropertyManager propertyManager;
    private final FFprobeManager ffprobeManager;
    private final FFmpegContextConfiguration ffmpegContextConfiguration;

    @Autowired
    protected FFmpegManagerImpl(PropertyManager propertyManager, FFprobeManager ffprobeManager, FFmpegContextConfiguration ffmpegContextConfiguration) {
        this.propertyManager = propertyManager;
        this.ffprobeManager = ffprobeManager;
        this.ffmpegContextConfiguration = ffmpegContextConfiguration;
    }

    @Override
    protected Process convertToHls(Path storePath, Path videoPath) throws IOException {
        String ffmpegPath = propertyManager.getFfmpegPath();
        FFprobeResult analyze = ffprobeManager.analyze(videoPath);

        var command = FFmpegCommand.newCommand(ffmpegContextConfiguration.getHlsConvertConfig(),
                ffmpegPath, storePath, videoPath, analyze)
            .buildCommand();

        return new ProcessBuilder(command)
            .redirectErrorStream(true)
            .start();
    }

    @Override
    protected Path convertToWebP(Path beforeImage, Path afterImage, int quality) throws IOException {

        String ffmpegPath = propertyManager.getFfmpegPath();
        int validQuality = Math.min(100, Math.max(0, quality));

        List<String> command = List.of(
            ffmpegPath,
            "-i", beforeImage.toString(),
            "-quality", String.valueOf(validQuality),
            "-y",  // 기존 파일 덮어쓰기
            afterImage.toString()
        );

        Process process = new ProcessBuilder(command)
            .redirectErrorStream(true)
            .start();

        // 프로세스 완료 대기
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg process failed with exit code: " + exitCode);
            }
            return afterImage;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg process interrupted", e);
        }
    }

}
