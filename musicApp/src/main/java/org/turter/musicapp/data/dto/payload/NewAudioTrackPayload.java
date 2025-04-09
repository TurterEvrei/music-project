package org.turter.musicapp.data.dto.payload;

public record NewAudioTrackPayload(
        String title,
        byte[] data
) {
}
