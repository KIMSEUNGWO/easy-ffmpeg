package com.jours.easy_ffmpeg.config;

import com.jours.easy_ffmpeg.secure.EncryptionKey;

public class HlsConvertConfig {

    private final boolean debug;
    private final int hlsTime;
    private final int listSize;
    private final String streamName;
    private final String segmentName;
    private final String masterName;
    private final SegmentType segmentType;  // mpegts or fmp4
    private final int keyFrameInterval;       // 키프레임 간격
    private final boolean encryption;      // HLS 암호화 여부
    private final EncryptionKey encryptionKey;      // 암호화 키 정보
    private final String playlistType;      // VOD or EVENT
    private final boolean discontinuity;   // 세그먼트 불연속성 표시
    private final int startNumber;            // 세그먼트 시작 번호
    private final int segmentDigits;        // 세그먼트 번호의 자릿수

    HlsConvertConfig(HlsConvertBuilder builder) {
        this.debug = builder.isDebug();
        this.hlsTime = builder.getHlsTime();
        this.listSize = builder.getListSize();
        this.streamName = builder.getStreamName();
        this.segmentName = builder.getSegmentName();
        this.masterName = builder.getMasterName();
        this.segmentType = builder.getSegmentType();
        this.keyFrameInterval = builder.getKeyFrameInterval();
        this.encryption = builder.isEncryption();
        this.encryptionKey = builder.getEncryptionKey();
        this.playlistType = builder.getPlaylistType();
        this.discontinuity = builder.isDiscontinuity();
        this.startNumber = builder.getStartNumber();
        this.segmentDigits = builder.getSegmentDigits();
    }

    public boolean isDebug() {
        return debug;
    }

    public int getHlsTime() {
        return hlsTime;
    }

    public int getListSize() {
        return listSize;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public String getMasterName() {
        return masterName;
    }

    public String getSegmentType() {
        return segmentType.getType();
    }

    public int getKeyFrameInterval() {
        return keyFrameInterval;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public EncryptionKey getEncryptionKey() {
        return encryptionKey;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public boolean isDiscontinuity() {
        return discontinuity;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public int getSegmentDigits() {
        return segmentDigits;
    }
}
