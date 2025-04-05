package org.turter.musicapp.domain;

import java.util.Objects;

public class AudioTrack {
    private Long remoteId;
    private String filePath;
    private String name;
    private long durationMs;

    public AudioTrack(String filePath, String name, long durationMs) {
        this(null, filePath, name, durationMs);
    }

    public AudioTrack(Long remoteId, String filePath, String name, long durationMs) {
        this.remoteId = remoteId;
        this.filePath = filePath;
        this.name = name;
        this.durationMs = durationMs;
    }

    public String getFilePath() {
        return filePath;
    }

    public AudioTrack setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getName() {
        return name;
    }

    public AudioTrack setName(String name) {
        this.name = name;
        return this;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public AudioTrack setDurationMs(long durationMs) {
        this.durationMs = durationMs;
        return this;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public AudioTrack setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioTrack that = (AudioTrack) o;
        return durationMs == that.durationMs && Objects.equals(remoteId, that.remoteId) && Objects.equals(filePath, that.filePath) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteId, filePath, name, durationMs);
    }
}
