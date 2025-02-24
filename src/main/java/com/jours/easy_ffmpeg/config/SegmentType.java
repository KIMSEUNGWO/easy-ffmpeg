package com.jours.easy_ffmpeg.config;

public enum SegmentType {

    MPEGTS("mpegts"),
    FMP4("fmp4");

    private String type;

    SegmentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
