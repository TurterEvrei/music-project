package org.turter.musiccatalogue.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CompositionPayload(
        @NotNull(message = "Id должен присутствовать")
        Long id,
        @NotBlank(message = "Название не должно быть пустым")
        String title,
        @NotNull(message = "Список треков не должен быть null")
        Set<TrackClipPayload> tracks
) {
}
