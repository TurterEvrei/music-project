package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.payload.NewCompositionPayload;
import org.turter.musicapp.data.remote.client.CompositionApiClient;
import org.turter.musicapp.data.remote.client.CompositionApiClientImpl;

import java.util.Collections;

public class CompositionCreateService extends Service<CompositionInfoDto> {
    private final CompositionApiClient client = CompositionApiClientImpl.getInstance();
    private final String title;

    public CompositionCreateService(String title) {
        this.title = title;
    }

    @Override
    protected Task<CompositionInfoDto> createTask() {
        return new Task<>() {
            @Override
            protected CompositionInfoDto call() throws Exception {
                CompositionInfoDto result = client.createComposition(new NewCompositionPayload(title,
                        Collections.emptySet()));
                if (result != null) {
                    System.out.printf("Create composition: %s", result.title());
                } else {
                    System.out.println("Api response is empty");
                }
                return result;
            }
        };
    }
}
