package com.jours.easy_ffmpeg.secure;

import java.nio.file.Path;

public class EncryptionKey {

    private final Path keyFilePath;
    private int keySize = 16;
    private final Path keyInfoPath;
    private final String keyServerPath;
    private boolean fileReplace = false;

    public EncryptionKey(Path keyFilePath, Path keyInfoPath, String keyServerPath, int keySize, boolean fileReplace) {
        this.keyFilePath = keyFilePath;
        this.keyInfoPath = keyInfoPath;
        this.keyServerPath = keyServerPath;
        this.keySize = keySize;
        this.fileReplace = fileReplace;
    }

    public boolean isFileReplace() {
        return fileReplace;
    }

    public Path getKeyFilePath() {
        return keyFilePath;
    }

    public int getKeySize() {
        return keySize;
    }

    public Path getKeyInfoPath() {
        return keyInfoPath;
    }

    public String getKeyServerPath() {
        return keyServerPath;
    }

    public void validate() {
        if (keySize <= 0) {
            throw new IllegalStateException("Key size must be greater than 0");
        }
        if (keyServerPath == null || keyServerPath.isBlank()) {
            throw new IllegalStateException("Key server path must be not blank");
        }
    }

    public static EncryptionKeyBuilder builder() {
        return new EncryptionKeyBuilder();
    }

    public static class EncryptionKeyBuilder {
        private Path keyFilePath;
        private int keySize = 16;
        private Path keyInfoPath;
        private String keyServerPath;
        private boolean fileReplace = false;

        public EncryptionKeyBuilder keyFilePath(Path keyFilePath) {
            this.keyFilePath = keyFilePath;
            return this;
        }

        public EncryptionKeyBuilder keySize(int keySize) {
            this.keySize = keySize;
            return this;
        }

        public EncryptionKeyBuilder keyInfoPath(Path keyInfoPath) {
            this.keyInfoPath = keyInfoPath;
            return this;
        }

        public EncryptionKeyBuilder keyServerPath(String keyServerPath) {
            this.keyServerPath = keyServerPath;
            return this;
        }

        public EncryptionKeyBuilder fileReplace(boolean fileReplace) {
            this.fileReplace = fileReplace;
            return this;
        }

        public EncryptionKey build() {
            return new EncryptionKey(keyFilePath, keyInfoPath, keyServerPath, keySize, fileReplace);
        }

    }

}
