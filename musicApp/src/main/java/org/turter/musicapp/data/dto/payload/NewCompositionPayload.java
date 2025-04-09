package org.turter.musicapp.data.dto.payload;

import java.util.Set;

public record NewCompositionPayload(
        String title,
        Set<NewTrackClipPayload> tracks
) {
}
