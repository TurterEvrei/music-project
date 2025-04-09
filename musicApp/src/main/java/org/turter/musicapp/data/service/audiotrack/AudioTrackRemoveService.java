package org.turter.musicapp.data.service.audiotrack;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.AudioTrackApiClient;
import org.turter.musicapp.data.remote.client.AudioTrackApiClientImpl;
import org.turter.musicapp.domain.AudioTrack;

import java.io.IOException;

public class AudioTrackRemoveService extends Service<Void> {
    private final AudioTrack track;
    private final AudioTrackApiClient client = AudioTrackApiClientImpl.getInstance();

    public AudioTrackRemoveService(AudioTrack track) {
        this.track = track;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (client.deleteTrack(track.getRemoteId())) {
                    TrackCacheStore.removeTrack(track.getRemoteId().toString());
                } else {
                    throw new IOException("Ошибка при удалении");
                }
                return null;
            }
        };
    }
}

