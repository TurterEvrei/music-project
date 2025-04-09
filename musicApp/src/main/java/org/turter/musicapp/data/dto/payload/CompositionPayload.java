package org.turter.musicapp.data.dto.payload;

import java.util.Set;

public record CompositionPayload(
        Long id,
        String title,
        Set<TrackClipPayload> tracks
) {
}
