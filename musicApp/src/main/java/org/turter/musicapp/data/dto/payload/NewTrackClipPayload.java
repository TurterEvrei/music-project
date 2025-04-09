package org.turter.musicapp.data.dto.payload;

public record NewTrackClipPayload(
        Long startTimeMs,
        Double volume,
        Long durationMs,
        Long audioTrackId
) {
}