package org.turter.musicapp.domain;

import java.util.Objects;

public class TrackClip {
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

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }

    public TrackClip setAudioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
        return this;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public double getVolume() {
        return volume;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public TrackClip setDurationMs(long durationMs) {
        this.durationMs = durationMs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackClip trackClip = (TrackClip) o;
        return startTimeMs == trackClip.startTimeMs && Double.compare(volume, trackClip.volume) == 0 && durationMs == trackClip.durationMs && Objects.equals(audioTrack, trackClip.audioTrack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audioTrack, startTimeMs, volume, durationMs);
    }
}
