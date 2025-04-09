package org.turter.musiccatalogue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.turter.musiccatalogue.dto.AudioTrackDto;
import org.turter.musiccatalogue.dto.payload.AudioTrackPayload;
import org.turter.musiccatalogue.dto.payload.NewAudioTrackPayload;
import org.turter.musiccatalogue.entity.AudioTrack;
import org.turter.musiccatalogue.service.AudioTrackService;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class AudioTrackController {
    private final AudioTrackService service;

    @GetMapping
    public List<AudioTrackDto> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AudioTrackDto get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @PostMapping
    public AudioTrackDto create(@Valid @RequestBody NewAudioTrackPayload payload) {
        return service.save(payload);
    }

    @PutMapping
    public AudioTrackDto update(@Valid @RequestBody AudioTrackPayload payload) {
        return service.update(payload);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
