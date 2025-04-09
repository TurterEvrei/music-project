package org.turter.musicapp.data.service.composition;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.payload.CompositionPayload;
import org.turter.musicapp.data.dto.payload.NewCompositionPayload;
import org.turter.musicapp.data.mapper.CompositionMapper;
import org.turter.musicapp.data.remote.client.CompositionApiClient;
import org.turter.musicapp.data.remote.client.CompositionApiClientImpl;
import org.turter.musicapp.domain.Composition;

import java.util.Collections;

public class CompositionSaveService extends Service<CompositionInfoDto> {
    private final CompositionApiClient client = CompositionApiClientImpl.getInstance();
    private final Composition composition;

    public CompositionSaveService(Composition composition) {
        this.composition = composition;
    }

    @Override
    protected Task<CompositionInfoDto> createTask() {
        return new Task<>() {
            @Override
            protected CompositionInfoDto call() throws Exception {
                CompositionInfoDto result;
                if (composition.getId() == null) {
                    result = client.createComposition(CompositionMapper.toNewPayload(composition));
                } else {
                    result = client.updateComposition(CompositionMapper.toPayload(composition));
                }

                if (result != null) {
                    System.out.printf("Save composition: %s", result.title());
                } else {
                    System.out.println("Api response is empty");
                }
                return result;
            }
        };
    }
}
