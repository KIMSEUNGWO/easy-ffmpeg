package com.jours.easy_ffmpeg.config;

public interface FFmpegConfiguration {

    default HlsConvertConfig hlsConfig(HlsConvertBuilder builder) {
        return builder.build();
    }

}
