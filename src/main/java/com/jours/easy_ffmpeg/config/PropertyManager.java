package com.jours.easy_ffmpeg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyManager {

    @Value("${path.ffmpeg}")
    private String ffmpegPath;

    @Value("${path.ffprobe}")
    private String ffprobePath;

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    public String getFfprobePath() {
        return ffprobePath;
    }
}
