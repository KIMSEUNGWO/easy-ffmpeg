package com.jours.easy_ffmpeg.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;

@Component
public class EncryptionKeyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionKeyGenerator.class);

    public void generateKey(EncryptionKey encryptionKey) {
        try {
            boolean generated = generateEncryptionKey(encryptionKey);
            if (generated) {
                generateKeyInfo(encryptionKey);
                logger.info("Encryption key generated successfully");
            }
        } catch (Exception e) {
            logger.warn("Error generating encryption key", e);
        }
    }

    private void generateKeyInfo(EncryptionKey encryptionKey) throws IOException {
        List<String> lines = generateCommandLines(encryptionKey);
        Files.write(encryptionKey.getKeyInfoPath(), lines);
    }

    private List<String> generateCommandLines(EncryptionKey encryptionKey) {
        return List.of(
            encryptionKey.getKeyServerPath(),
            encryptionKey.getKeyFilePath().toString()
        );
    }

    private boolean generateEncryptionKey(EncryptionKey encryptionKey) throws IOException {

        Path keyPath = encryptionKey.getKeyFilePath();

        if (!encryptionKey.isFileReplace() && Files.exists(keyPath)) {
            logger.info("Encryption key already exists");
            return false;
        }

        logger.info("Generating new encryption key...");

        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[encryptionKey.getKeySize()];
        secureRandom.nextBytes(key);

        Files.write(keyPath, key);
        return true;
    }


}