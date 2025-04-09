package org.turter.musicapp.data.service.composition;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import org.turter.musicapp.domain.TrackClip;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CompositionMixer {
    private static String buildMixFilter(List<TrackClip> tracks) {
        StringBuilder filter = new StringBuilder();
        List<String> processedStreams = new ArrayList<>();

        for (int i = 0; i < tracks.size(); i++) {
            TrackClip track = tracks.get(i);
            String streamTitle = String.format("custom%d", i);
            filter.append(String.format(
                    Locale.US,
                    "[%d:a]adelay=%d|%d,volume=%.1f[%s];",
                    i,
                    track.getStartTimeMs(),
                    track.getStartTimeMs(),
                    track.getVolume(),
                    streamTitle
            ));
            processedStreams.add("[" + streamTitle + "]");
        }

        String inputs = String.join("", processedStreams);
        filter.append(String.format(
                "%samix=inputs=%d:duration=longest",
                inputs,
                tracks.size()
        ));

        return filter.toString();
    }

    public static void mixTracks(List<TrackClip> tracks, String outputPath, AudioFormat format) {
        FFmpeg ffmpeg = FFmpeg.atPath(Paths.get("musicApp/ffmpeg/bin"))
                .setOverwriteOutput(true);

        for (TrackClip track : tracks) {
            ffmpeg.addInput(UrlInput.fromUrl(track.getAudioTrack().getFilePath()));
        }

        String filter = buildMixFilter(tracks);
        ffmpeg.setComplexFilter(filter);

        UrlOutput output = UrlOutput.toUrl(ensureExtension(outputPath, format))
                .setCodec("a", format.codec);

        format.options.forEach((key, value) ->
                output.addArguments("-" + key, value)
        );

        ffmpeg.addOutput(output);
        ffmpeg.execute();
    }

    private static String ensureExtension(String path, AudioFormat format) {
        if (path.toLowerCase().endsWith(format.extension)) {
            return path;
        }
        return path + format.extension;
    }

    public enum AudioFormat {
        MP3("libmp3lame", ".mp3", Map.of("q:a", "2")),
        FLAC("flac", ".flac", Map.of()),
        WAV("pcm_s16le", ".wav", Map.of());

        private final String codec;
        private final String extension;
        private final Map<String, String> options;

        AudioFormat(String codec, String extension, Map<String, String> options) {
            this.codec = codec;
            this.extension = extension;
            this.options = options;
        }

        public static AudioFormat getByExtension(String filename) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".mp3")) return CompositionMixer.AudioFormat.MP3;
            if (lower.endsWith(".flac")) return CompositionMixer.AudioFormat.FLAC;
            if (lower.endsWith(".wav")) return CompositionMixer.AudioFormat.WAV;
            return null;
        }
    }
}
