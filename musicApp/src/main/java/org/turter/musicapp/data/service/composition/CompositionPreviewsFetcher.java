package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.CompositionPreviewDto;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.remote.client.AudioTrackApiClient;
import org.turter.musicapp.data.remote.client.AudioTrackApiClientImpl;
import org.turter.musicapp.data.remote.client.CompositionApiClient;
import org.turter.musicapp.data.remote.client.CompositionApiClientImpl;
import org.turter.musicapp.domain.AudioTrack;

import java.util.List;

public class CompositionPreviewsFetcher extends Service<List<CompositionPreviewDto>> {
    private final CompositionApiClient client = CompositionApiClientImpl.getInstance();

    @Override
    protected Task<List<CompositionPreviewDto>> createTask() {
        return new Task<>() {
            @Override
            protected List<CompositionPreviewDto> call() throws Exception {
                List<CompositionPreviewDto> result = client.getCompositionPreviews();
                if (!result.isEmpty()) {
                    System.out.printf("Fetched composition previews from api: %d elements.%n",
                            result.size());
                } else {
                    System.out.println("Api response is empty");
                }
                return result;
            }
        };
    }
}
