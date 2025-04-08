package org.turter.musiccatalogue.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AudioTrackPayload(
        @NotNull(message = "Id должен присутствовать")
        Long id,
        @NotBlank(message = "Название не должно быть пустым")
        String title,
        @NotNull(message = "Данные трека должны присутствовать")
        byte[] data
) {
}
