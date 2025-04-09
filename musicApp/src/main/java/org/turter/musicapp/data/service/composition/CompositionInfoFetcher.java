package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.CompositionPreviewDto;
import org.turter.musicapp.data.remote.client.CompositionApiClient;
import org.turter.musicapp.data.remote.client.CompositionApiClientImpl;

import java.util.List;

public class CompositionInfoFetcher extends Service<CompositionInfoDto> {
    private final CompositionApiClient client = CompositionApiClientImpl.getInstance();
    private final Long id;

    public CompositionInfoFetcher(Long id) {
        this.id = id;
    }

    @Override
    protected Task<CompositionInfoDto> createTask() {
        return new Task<>() {
            @Override
            protected CompositionInfoDto call() throws Exception {
                CompositionInfoDto result = client.getCompositionInfo(id);
                if (result != null) {
                    System.out.printf("Fetched composition info from api: %s", result.title());
                } else {
                    System.out.println("Api response is empty");
                }
                return result;
            }
        };
    }
}
