package org.turter.musicapp.data.dto;

public record AudioTrackDto(
        Long id,
        String title,
        byte[] data
) {
}
