package org.turter.musicapp.data.utils;

import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.UrlInput;

import java.nio.file.Paths;

public class AudioUtils {

    public static long getDurationMs(String path) {
        long durationMs = 0;
        FFprobeResult result = FFprobe.atPath(Paths.get("musicApp/ffmpeg/bin").toAbsolutePath())
                .setInput(UrlInput.fromUrl(path))
                .setShowStreams(true)
                .execute();
        durationMs = (long) (result.getStreams().get(0).getDuration() * 1000);
        return durationMs;
    }

}
