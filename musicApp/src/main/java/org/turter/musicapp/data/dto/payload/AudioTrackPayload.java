package org.turter.musicapp.data.dto.payload;

public record AudioTrackPayload(
        Long id,
        String title,
        byte[] data
) {
}
