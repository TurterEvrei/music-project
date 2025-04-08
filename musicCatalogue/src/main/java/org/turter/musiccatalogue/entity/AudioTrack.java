package org.turter.musiccatalogue.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "audio_tracks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudioTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "data")
    private byte[] data;
}
