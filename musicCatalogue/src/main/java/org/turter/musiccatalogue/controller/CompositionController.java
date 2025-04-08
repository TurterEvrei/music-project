package org.turter.musiccatalogue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.turter.musiccatalogue.dto.payload.CompositionPayload;
import org.turter.musiccatalogue.dto.payload.NewCompositionPayload;
import org.turter.musiccatalogue.dto.CompositionInfo;
import org.turter.musiccatalogue.dto.CompositionPreview;
import org.turter.musiccatalogue.service.CompositionService;

import java.util.List;

@RestController
@RequestMapping("/api/compositions")
@RequiredArgsConstructor
public class CompositionController {
    private final CompositionService service;

    @GetMapping("/info")
    public List<CompositionInfo> getAllInfos() {
        return service.getAllInfos();
    }

    @GetMapping("/info/{id}")
    public CompositionInfo getInfo(@PathVariable("id") Long id) {
        return service.getInfo(id);
    }

    @GetMapping("/preview")
    public List<CompositionPreview> getAllPreviews() {
        return service.getAllPreviews();
    }

    @GetMapping("/preview/{id}")
    public CompositionPreview getPreview(@PathVariable("id") Long id) {
        return service.getPreview(id);
    }

    @PostMapping
    public ResponseEntity<CompositionInfo> create(@Valid @RequestBody NewCompositionPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(payload));
    }

    @PutMapping("/{id}")
    public CompositionInfo update(@Valid @RequestBody CompositionPayload payload) {
        return service.update(payload);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
