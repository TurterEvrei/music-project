package org.turter.musicapp.data.service.audiotrack;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.payload.AudioTrackPayload;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.AudioTrackApiClient;
import org.turter.musicapp.data.remote.client.AudioTrackApiClientImpl;
import org.turter.musicapp.domain.AudioTrack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AudioTrackEditService extends Service<Void> {
    private final AudioTrack track;
    private final AudioTrackApiClient client = AudioTrackApiClientImpl.getInstance();

    public AudioTrackEditService(AudioTrack track) {
        this.track = track;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                byte[] fileData = Files.readAllBytes(Path.of(track.getFilePath()));
                AudioTrackPayload payload = new AudioTrackPayload(track.getRemoteId(), track.getName(), fileData);

                AudioTrackDto result = client.updateTrack(payload);
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

