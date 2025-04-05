package org.turter.musicapp.data.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.domain.Composition;

public class CompositionExportService extends Service<Void> {
    private final String outputPath;
    private final Composition composition;
    private final CompositionMixer.AudioFormat format;

    public CompositionExportService(String outputPath, Composition composition, CompositionMixer.AudioFormat format) {
        this.outputPath = outputPath;
        this.composition = composition;
        this.format = format;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    CompositionMixer.mixTracks(composition.getTracks(), outputPath, format);
                    composition.setResultFilePath(outputPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                return null;
            }
        };
    }
}
