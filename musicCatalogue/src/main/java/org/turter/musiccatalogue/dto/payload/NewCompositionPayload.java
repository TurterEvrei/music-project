package org.turter.musiccatalogue.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.turter.musiccatalogue.dto.AudioTrackDto;

import java.util.Set;

public record NewCompositionPayload(
        @NotBlank(message = "Название не должно быть пустым")
        String title,
        @NotNull(message = "Список треков не должен быть null")
        Set<NewTrackClipPayload> tracks
) {
}
