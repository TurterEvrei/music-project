package org.turter.musicapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Composition {
    private Long id;
    private String title;
    private List<TrackClip> tracks = new ArrayList<>();
    private String resultFilePath;

    public Composition(String title) {
        this.title = title;
    }

    public Composition addTrack(TrackClip clip) {
        tracks.add(clip);
        return this;
    }

    public Composition removeTrack(TrackClip clip) {
        tracks.remove(clip);
        return this;
    }

    public List<TrackClip> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public boolean isNew() {
        return id == null || id == 0;
    }

    public double getTotalDurationMs() {
        return tracks.stream()
                .mapToDouble(clip -> clip.getStartTimeMs() + clip.getDurationMs())
                .max()
                .orElse(0);
    }
}
