package org.turter.entity;

import javax.persistence.*;

@Entity
@Table(name = "tracks")
public class AudioTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "data")
    private byte[] data;

    public AudioTrack() {
    }

    public Long getId() {
        return id;
    }

    public AudioTrack setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AudioTrack setTitle(String title) {
        this.title = title;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public AudioTrack setData(byte[] data) {
        this.data = data;
        return this;
    }
}
