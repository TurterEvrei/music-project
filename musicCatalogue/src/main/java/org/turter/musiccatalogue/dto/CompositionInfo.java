package org.turter.musiccatalogue.dto;

import java.util.Set;

public record CompositionInfo(
        long id,
        String title,
        Set<TrackClipDto> tracks
) {
}
