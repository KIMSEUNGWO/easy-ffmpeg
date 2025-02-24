package com.jours.easy_ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

@Component
public class DirectoryManager {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleFileVisitor<Path> deleteVisitor;

    @Autowired
    public DirectoryManager(SimpleFileVisitor<Path> deleteVisitor) {
        this.deleteVisitor = deleteVisitor;
    }

    public void deleteIfExists(Path path) {
        try {
            if (Files.isDirectory(path)) {
                Files.walkFileTree(path, deleteVisitor);
            } else {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            log.error("Failed to delete: {}", path, e);
        }
    }

}
