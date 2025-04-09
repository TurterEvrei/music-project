package org.turter.musicapp.data.dto;

import java.util.Set;

public record CompositionInfoDto(
        Long id,
        String title,
        Set<TrackClipDto> tracks
) {
}
