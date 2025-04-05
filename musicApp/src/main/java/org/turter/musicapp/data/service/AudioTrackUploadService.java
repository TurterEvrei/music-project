package org.turter.musicapp.data.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.TrackApiClient;
import org.turter.musicapp.data.remote.client.TrackApiClientImpl;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.NewAudioTrackDto;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;

public class AudioTrackUploadService extends Service<Void> {
    private final File selectedFile;
    private final TrackApiClient client = TrackApiClientImpl.getInstance();

    public AudioTrackUploadService(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                NewAudioTrackDto trackDto = new NewAudioTrackDto(selectedFile.getName(), fileData);

                // Отправка HTTP-запроса
                AudioTrackDto result = client.createTrack(trackDto);
                if (result != null) {
                    TrackCacheStore.saveTrack(result);
                } else {
                    throw new IOException("Ошибка при загрузке");
                }
                return null;
            }
        };
    }
}

