package org.turter.musicapp.data.remote.client;

import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.CompositionPreviewDto;
import org.turter.musicapp.data.dto.payload.CompositionPayload;
import org.turter.musicapp.data.dto.payload.NewCompositionPayload;

import java.util.List;

public interface CompositionApiClient {
    List<CompositionPreviewDto> getCompositionPreviews();
    CompositionInfoDto getCompositionInfo(Long id);
    CompositionInfoDto createComposition(NewCompositionPayload payload);
    CompositionInfoDto updateComposition(CompositionPayload payload);
    boolean deleteComposition(Long id);
}
