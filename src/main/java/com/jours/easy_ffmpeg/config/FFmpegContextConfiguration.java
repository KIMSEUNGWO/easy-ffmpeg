package com.jours.easy_ffmpeg.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FFmpegContextConfiguration {

    private FFmpegConfiguration ffmpegConfiguration;
    private HlsConvertConfig hlsConvertConfig;

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
    }

    public HlsConvertConfig getHlsConvertConfig() {
        return hlsConvertConfig;
    }
}
