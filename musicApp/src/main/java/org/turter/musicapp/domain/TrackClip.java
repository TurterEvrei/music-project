package org.turter.musicapp.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class TrackClip {
    private Long remoteId;
    private AudioTrack audioTrack;
    private long startTimeMs;
    private double volume;
    private long durationMs;

    public TrackClip(AudioTrack audioTrack, long startTimeMs, long durationMs) {
        this(audioTrack, startTimeMs, durationMs, 1.0);
    }

    public TrackClip(AudioTrack audioTrack, long startTimeMs, long durationMs, double volume) {
        this.audioTrack = audioTrack;
        this.startTimeMs = startTimeMs;
        this.durationMs = durationMs;
        this.volume = Math.max(0.0, Math.min(1.0, volume));
    }

    public TrackClip setStartTimeMs(long startTimeMs) {
        this.startTimeMs = Math.max(0, startTimeMs);
        return this;
    }

    public TrackClip setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        return this;
    }
}
