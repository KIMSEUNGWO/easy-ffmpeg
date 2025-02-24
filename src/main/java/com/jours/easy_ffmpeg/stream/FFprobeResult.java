package com.jours.easy_ffmpeg.stream;

public record FFprobeResult(int width, int height, double fps, boolean hasAudio) {

    public VideoStream toVideoStream() {
        return new VideoStream(width, height, fps);
    }
    public AudioStream toAudioStream() {
        return new AudioStream(hasAudio);
    }
}
