package org.turter.musiccatalogue.dto;

public record TrackClipDto(
        long id,
        long startTimeMs,
        double volume,
        long durationMs,
        AudioTrackDto audioTrack
) {
}
