package org.turter.musicapp.data.dto;

public record NewAudioTrackDto(
        String title,
        byte[] data
) {
}
