package org.turter.musicapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudioTrack {
    private Long remoteId;
    private String filePath;
    private String name;
    private long durationMs;

    public AudioTrack(String filePath, String name, long durationMs) {
        this(null, filePath, name, durationMs);
    }
}
