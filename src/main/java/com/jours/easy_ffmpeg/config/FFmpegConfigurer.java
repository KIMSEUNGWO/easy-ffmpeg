package com.jours.easy_ffmpeg.config;

import java.util.function.Function;

@FunctionalInterface
public interface FFmpegConfigurer<T> extends Function<T, T> {

}
