package org.turter.musicapp.data.service.audiotrack;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.AudioTrackApiClient;
import org.turter.musicapp.data.remote.client.AudioTrackApiClientImpl;
import org.turter.musicapp.domain.AudioTrack;
import org.turter.musicapp.data.dto.AudioTrackDto;

import java.util.List;

public class AudioTracksUpdateService extends Service<List<AudioTrack>> {
    private final AudioTrackApiClient client = AudioTrackApiClientImpl.getInstance();

    @Override
    protected Task<List<AudioTrack>> createTask() {
        return new Task<>() {
            @Override
            protected List<AudioTrack> call() throws Exception {
                List<AudioTrackDto> result = client.getAllTracks();
                if (!result.isEmpty()) {
                    System.out.printf("Fetched tracks from api: %d elements. Start replacing cached tracks%n",
                            result.size());
                    TrackCacheStore.evictCache();
                    TrackCacheStore.saveTracks(result);
                } else {
                    System.out.println("Api response is empty");
                }
                return TrackCacheStore.getCachedTracks();
            }
        };
    }
}
