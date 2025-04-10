package org.turter.musiccatalogue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "track_clips")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackClip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time_ms")
    private Long startTimeMs;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "duration_ms")
    private Long durationMs;

    @ManyToOne
    @JoinColumn(name = "audio_track_id")
    private AudioTrack audioTrack;

    @ManyToOne
    @JoinColumn(name = "composition_id")
    private Composition composition;

    public boolean isNew() {
        return id == null || id == 0;
    }
}
