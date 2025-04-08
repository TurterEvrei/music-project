package org.turter.musiccatalogue.dto.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewTrackClipPayload(
        @Min(value = 0, message = "Время старта не должно быть меньше 0")
        Long startTimeMs,
        @Min(value = 0, message = "Громкость не должна быть меньше 0")
        Double volume,
        @Min(value = 0, message = "Продолжительность не должна быть меньше 0")
        Long durationMs,
        @NotNull(message = "Id аудио трека должно присутствовать")
        Long audioTrackId
) {
}