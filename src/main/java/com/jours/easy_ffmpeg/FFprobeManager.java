package com.jours.easy_ffmpeg;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jours.easy_ffmpeg.config.PropertyManager;
import com.jours.easy_ffmpeg.stream.FFprobeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@Component
class FFprobeManager {

    private final ObjectMapper mapper;
    private final PropertyManager propertyManager;

    @Autowired
    protected FFprobeManager(PropertyManager propertyManager) {
        this.mapper = new ObjectMapper();
        this.propertyManager = propertyManager;
    }

    protected FFprobeResult analyze(Path originalVideoPath) throws IOException {

        try {
            // FFprobe 실행
            Process process = convert(originalVideoPath);

            // 결과 읽기
            String json = readJson(process);

            // 프로세스 완료 대기
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFprobe failed with exit code: " + exitCode);
            }

            return parseFFprobeResult(json);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFprobe process interrupted", e);
        }
    }

    private String readJson(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        }
    }

    private Process convert(Path originalVideoPath) throws IOException {
        String ffprobePath = propertyManager.getFfprobePath();

        List<String> command = List.of(
            ffprobePath,
            "-v", "error",
            "-show_entries", "stream=codec_type,width,height,r_frame_rate",
            "-of", "json",
            originalVideoPath.toString()
        );

        return new ProcessBuilder(command)
            .redirectErrorStream(true)
            .start();
    }

    private FFprobeResult parseFFprobeResult(String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        JsonNode streams = root.path("streams");

        // 비디오 스트림 찾기
        JsonNode videoStream = null;
        boolean hasAudio = false;

        for (JsonNode stream : streams) {
            String codecType = stream.path("codec_type").asText();
            switch (codecType) {
                case "video" -> videoStream = stream;
                case "audio" -> hasAudio = true;
            }
        }

        if (videoStream == null) {
            throw new IOException("No video stream found");
        }

        int width = videoStream.path("width").asInt();
        int height = videoStream.path("height").asInt();
        double fps = calculateFps(videoStream.path("r_frame_rate").asText());

        return new FFprobeResult(width, height, fps, hasAudio);
    }

    private double calculateFps(String frameRate) {
        String[] parts = frameRate.split("/");
        if (parts.length == 2) {
            double numerator = Double.parseDouble(parts[0]);
            double denominator = Double.parseDouble(parts[1]);
            return numerator / denominator;
        }
        return Double.parseDouble(frameRate);
    }

}
