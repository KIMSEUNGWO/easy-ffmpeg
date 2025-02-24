package com.jours.easy_ffmpeg;

import com.jours.easy_ffmpeg.config.FFmpegContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FFmpegConverterImpl implements FFmpegConverter {

    private final FFmpegManager ffmpegManager;
    private final DirectoryManager directoryManager;
    private final FFmpegContextConfiguration configuration;

    @Autowired
    public FFmpegConverterImpl(FFmpegManager ffmpegManager, DirectoryManager directoryManager, FFmpegContextConfiguration configuration) {
        this.ffmpegManager = ffmpegManager;
        this.directoryManager = directoryManager;
        this.configuration = configuration;
    }

    @Override
    public Path convertToHls(Path storePath, File file) throws IOException {

        try {
            Process process = ffmpegManager.convertToHls(Files.createDirectories(storePath), file.toPath());

            boolean debug = configuration.getHlsConvertConfig().isDebug();
            if (debug) {
                printLog(process);
            }

            // 프로세스 완료 대기
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("FFmpeg process failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            directoryManager.deleteIfExists(storePath);
            throw new IOException(e);
        }
        return storePath;
    }

    @Override
    public Path convertToWebP(MultipartFile image, Path outputPath, int quality) throws IOException {
        String originalFilename = image.getOriginalFilename();

        // 임시 파일 생성
        Path tempFile = Files.createTempFile("upload_", "_" + originalFilename);

        try {
            // MultipartFile을 임시 파일로 저장
            Files.copy(image.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // WebP로 변환
            return ffmpegManager.convertToWebP(tempFile, outputPath, quality);
        } finally {
            // 임시 파일 삭제
            Files.deleteIfExists(tempFile);
        }
    }


}
