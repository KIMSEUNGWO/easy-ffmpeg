package com.jours.easy_ffmpeg.config;

public enum PlaylistType {

    VOD("vod"),
    LIVE("event");

    private final String type;

    PlaylistType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
