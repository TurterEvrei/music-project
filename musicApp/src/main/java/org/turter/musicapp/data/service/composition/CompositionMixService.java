package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.domain.Composition;

public class CompositionMixService extends Service<String> {
    private static final String CACHE_DIR = "musicApp/src/main/resources/cache/compositions/";
    private final Composition composition;

    public CompositionMixService(Composition composition) {
        this.composition = composition;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String outputPath = CACHE_DIR + composition.getTitle() + ".mp3";
                    CompositionMixer.mixTracks(composition.getTracks(), outputPath, CompositionMixer.AudioFormat.MP3);
                    composition.setResultFilePath(outputPath);
                    return outputPath;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        };
    }

}
