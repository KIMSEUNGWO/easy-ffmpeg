package com.jours.easy_ffmpeg;

import java.io.IOException;
import java.nio.file.Path;

abstract class FFmpegManager {

    protected abstract Process convertToHls(Path storePath, Path videoPath) throws IOException;
    protected abstract Path convertToWebP(Path beforeImage, Path afterImage, int quality) throws IOException;
}
