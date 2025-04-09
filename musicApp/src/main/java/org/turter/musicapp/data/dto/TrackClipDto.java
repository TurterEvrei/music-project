package org.turter.musicapp.data.dto;

public record TrackClipDto(
        Long id,
        Long startTimeMs,
        Double volume,
        Long durationMs,
        AudioTrackDto audioTrack
) {
}
