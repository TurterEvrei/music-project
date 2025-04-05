package org.turter.musicapp.data.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.NewAudioTrackDto;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.TrackApiClient;
import org.turter.musicapp.data.remote.client.TrackApiClientImpl;
import org.turter.musicapp.domain.AudioTrack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AudioTrackRemoveService extends Service<Void> {
    private final AudioTrack track;
    private final TrackApiClient client = TrackApiClientImpl.getInstance();

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

