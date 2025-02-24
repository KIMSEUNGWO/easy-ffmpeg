package com.jours.easy_ffmpeg;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public interface FFmpegConverter {

    Path convertToHls(Path storePath, File file) throws IOException;
    Path convertToWebP(MultipartFile image, Path outputPath, int quality) throws IOException;

    default void printLog(Process process) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            reader.lines().forEach(line -> System.out.println("FFmpeg: " + line));
        }
    }

}
