package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.payload.NewCompositionPayload;
import org.turter.musicapp.data.remote.client.CompositionApiClient;
import org.turter.musicapp.data.remote.client.CompositionApiClientImpl;

import java.util.Collections;

public class CompositionDeleteService extends Service<Void> {
    private final CompositionApiClient client = CompositionApiClientImpl.getInstance();
    private final Long id;

    public CompositionDeleteService(Long id) {
        this.id = id;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (client.deleteComposition(id)) {
                    System.out.printf("Delete composition for id: %s", id);
                } else {
                    System.out.println("Api response is empty");
                }
                return null;
            }
        };
    }
}
