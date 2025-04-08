package org.turter.musiccatalogue.dto;

public record AudioTrackDto(
        long id,
        String title,
        byte[] data
) {
}
