package com.jours.easy_ffmpeg.config;

import com.jours.easy_ffmpeg.secure.EncryptionKey;

public class HlsConvertBuilder {

    private boolean debug = false;
    private int hlsTime = 10;
    private int listSize = 0;
    private String streamName = "stream";
    private String segmentName = "segment";
    private String masterName = "master";
    private SegmentType segmentType = SegmentType.MPEGTS;  // mpegts or fmp4
    private int keyFrameInterval = 2;       // 키프레임 간격
    private boolean encryption = false;      // HLS 암호화 여부
    private EncryptionKey encryptionKey = null;      // 암호화 키 정보
    private PlaylistType playlistType = PlaylistType.VOD;      // VOD or EVENT
    private boolean discontinuity = false;   // 세그먼트 불연속성 표시
    private int startNumber = 0;            // 세그먼트 시작 번호
    private int segmentDigits = 3;        // 세그먼트 번호의 자릿수

    public HlsConvertBuilder debug() {
        this.debug = true;
        return this;
    }
//    public HlsConvertBuilder segmentType(SegmentType type) {
//        this.segmentType = type;
//        return this;
//    }

    public HlsConvertBuilder keyFrameInterval(int interval) {
        this.keyFrameInterval = interval;
        return this;
    }

    public HlsConvertBuilder enableEncryption(FFmpegConfigurer<EncryptionKey.EncryptionKeyBuilder> encryptionKeyBuilder) {
        this.encryption = true;
        this.encryptionKey = encryptionKeyBuilder.apply(EncryptionKey.builder()).build();
        return this;
    }
    public HlsConvertBuilder enableEncryption() {
        this.encryption = true;
        this.encryptionKey = EncryptionKey.builder().build();
        return this;
    }

    public HlsConvertBuilder playlistType(PlaylistType type) {
        this.playlistType = type;
        return this;
    }

    public HlsConvertBuilder startNumber(int number) {
        this.startNumber = number;
        return this;
    }

    public HlsConvertBuilder enableDiscontinuity() {
        this.discontinuity = true;
        return this;
    }

    public HlsConvertBuilder segmentDigits(int digits) {
        this.segmentDigits = digits;
        return this;
    }

    public HlsConvertBuilder vodPreset() {
        this.hlsTime = 10;
        this.listSize = 0;
        this.playlistType = PlaylistType.VOD;
        return this;
    }

    public HlsConvertBuilder livePreset() {
        this.hlsTime = 5;
        this.listSize = 5;
        this.playlistType = PlaylistType.LIVE;
        return this;
    }

    public HlsConvertConfig build() {
        validateConfig();
        return new HlsConvertConfig(this);
    }

    private void validateConfig() {
        if (hlsTime <= 0) {
            throw new IllegalStateException("HLS time must be greater than 0");
        }
        if (listSize < 0) {
            throw new IllegalStateException("List size cannot be negative");
        }
        if (segmentDigits <= 0) {
            throw new IllegalStateException("Segment digits must be greater than 0");
        }
        if (encryption && encryptionKey == null) {
            throw new IllegalStateException("Key info file must be provided when encryption is enabled");
        }
        if (encryptionKey != null) {
            encryptionKey.validate();
        }
        if (playlistType == null) {
            throw new IllegalStateException("Playlist type must be either 'vod' or 'event'");
        }
        if (segmentType == null) {
            throw new IllegalStateException("Segment type must be either 'mpegts' or 'fmp4'");
        }
    }

    protected boolean isDebug() {
        return debug;
    }

    protected int getHlsTime() {
        return hlsTime;
    }

    protected int getListSize() {
        return listSize;
    }

    protected String getStreamName() {
        return streamName;
    }

    protected String getSegmentName() {
        return segmentName;
    }

    protected String getMasterName() {
        return masterName;
    }

    protected SegmentType getSegmentType() {
        return segmentType;
    }

    protected int getKeyFrameInterval() {
        return keyFrameInterval;
    }

    protected boolean isEncryption() {
        return encryption;
    }

    protected EncryptionKey getEncryptionKey() {
        return encryptionKey;
    }

    protected String getPlaylistType() {
        return playlistType.getType();
    }

    protected boolean isDiscontinuity() {
        return discontinuity;
    }

    protected int getStartNumber() {
        return startNumber;
    }

    protected int getSegmentDigits() {
        return segmentDigits;
    }
}
