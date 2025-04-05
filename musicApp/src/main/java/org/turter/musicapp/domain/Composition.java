package org.turter.musicapp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Composition {
    private String name;
    private final List<TrackClip> tracks = new ArrayList<>();
    private String resultFilePath;

    public Composition(String name) {
        this.name = name;
    }

    public boolean isResultFilePathExists() {
        return resultFilePath != null && !resultFilePath.isEmpty();
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public Composition setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
        return this;
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

    public double getTotalDurationMs() {
        return tracks.stream()
                .mapToDouble(clip -> clip.getStartTimeMs() + clip.getDurationMs())
                .max()
                .orElse(0);
    }

    public String getName() {
        return name;
    }

    public Composition setName(String name) {
        this.name = name;
        return this;
    }
}
