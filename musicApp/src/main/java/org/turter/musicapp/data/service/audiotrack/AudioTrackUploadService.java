package org.turter.musicapp.data.service.audiotrack;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.AudioTrackApiClient;
import org.turter.musicapp.data.remote.client.AudioTrackApiClientImpl;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.payload.NewAudioTrackPayload;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;

public class AudioTrackUploadService extends Service<Void> {
    private final File selectedFile;
    private final AudioTrackApiClient client = AudioTrackApiClientImpl.getInstance();

    public AudioTrackUploadService(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                NewAudioTrackPayload trackDto = new NewAudioTrackPayload(selectedFile.getName(), fileData);

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

