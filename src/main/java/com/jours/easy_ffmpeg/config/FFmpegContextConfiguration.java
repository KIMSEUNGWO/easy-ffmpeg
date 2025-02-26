package com.jours.easy_ffmpeg.config;

import com.jours.easy_ffmpeg.secure.EncryptionKey;
import com.jours.easy_ffmpeg.secure.EncryptionKeyGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FFmpegContextConfiguration {

    private FFmpegConfiguration ffmpegConfiguration;
    private HlsConvertConfig hlsConvertConfig;

    private final EncryptionKeyGenerator encryptionKeyGenerator;

    @Autowired
    public FFmpegContextConfiguration(EncryptionKeyGenerator encryptionKeyGenerator) {
        this.encryptionKeyGenerator = encryptionKeyGenerator;
    }

    @Autowired(required = false)
    public void setFfmpegConfiguration(FFmpegConfiguration ffmpegConfiguration) {
        this.ffmpegConfiguration = ffmpegConfiguration;
    }

    @PostConstruct
    private void onApplicationReady() {
        if (ffmpegConfiguration != null) {
            hlsConvertConfig = ffmpegConfiguration.hlsConfig(new HlsConvertBuilder());
        } else {
            hlsConvertConfig = new HlsConvertBuilder().build();
        }

        if (hlsConvertConfig.isEncryption()) {
            EncryptionKey encryptionKey = hlsConvertConfig.getEncryptionKey();
            encryptionKeyGenerator.generateKey(encryptionKey);
        }
    }

    public HlsConvertConfig getHlsConvertConfig() {
        return hlsConvertConfig;
    }
}
