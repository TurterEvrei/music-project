package org.turter.musicapp.data.mapper;

import org.turter.musicapp.domain.AudioTrack;
import org.turter.musicapp.domain.Composition;
import org.turter.musicapp.data.utils.AudioUtils;

import java.io.File;

public class CompositionMapper {

    public static AudioTrack toAudioTrack(Composition composition) {
        long durationMs = 0;
        String resultFilePath = composition.getResultFilePath();
        File file = new File(resultFilePath);
        if (file.exists() && file.isFile() && file.getName().endsWith(".mp3")) {
            try {
                durationMs = AudioUtils.getDurationMs(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AudioTrack(resultFilePath, composition.getName(), durationMs);
    }

}
