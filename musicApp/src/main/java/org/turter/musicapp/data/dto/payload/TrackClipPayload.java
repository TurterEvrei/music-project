package org.turter.musicapp.data.dto.payload;

public record TrackClipPayload(
        Long id,
        Long startTimeMs,
        Double volume,
        Long durationMs,
        Long audioTrackId
) {
}