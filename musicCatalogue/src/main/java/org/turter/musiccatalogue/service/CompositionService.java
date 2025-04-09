package org.turter.musiccatalogue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.turter.musiccatalogue.dto.payload.CompositionPayload;
import org.turter.musiccatalogue.dto.payload.NewCompositionPayload;
import org.turter.musiccatalogue.dto.CompositionInfo;
import org.turter.musiccatalogue.dto.CompositionPreview;
import org.turter.musiccatalogue.entity.Composition;
import org.turter.musiccatalogue.entity.TrackClip;
import org.turter.musiccatalogue.mapper.CompositionMapper;
import org.turter.musiccatalogue.mapper.TrackClipMapper;
import org.turter.musiccatalogue.repository.impl.AudioTrackDao;
import org.turter.musiccatalogue.repository.impl.CompositionDao;
import org.turter.musiccatalogue.repository.impl.TrackClipDao;
import org.turter.musiccatalogue.util.ExceptionSupplier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompositionService {
    private final CompositionDao compositionDao;
    private final TrackClipDao trackClipDao;
    private final AudioTrackDao audioTrackDao;
    private final CompositionMapper compositionMapper;
    private final TrackClipMapper trackClipMapper;

    public CompositionInfo getInfo(Long id) {
        return compositionMapper.toInfo(this.fetchById(id));
    }

    public CompositionPreview getPreview(Long id) {
        return compositionMapper.toPreview(this.fetchById(id));
    }

    public List<CompositionInfo> getAllInfos() {
        return compositionMapper.toInfos(this.fetchAll());
    }

    public List<CompositionPreview> getAllPreviews() {
        return compositionMapper.toPreviews(this.fetchAll());
    }

    public CompositionInfo save(NewCompositionPayload payload) {
        Composition entity = compositionMapper.toNewEntity(payload.title(), payload.tracks().stream()
                .map(p -> trackClipMapper.toNewEntity(p, audioTrackDao.findById(p.audioTrackId())))
                .collect(Collectors.toSet()));
        compositionDao.save(entity);
        return compositionMapper.toInfo(entity);
    }

    public CompositionInfo update(CompositionPayload payload) {
        Long compositionId = payload.id();
        Composition savedComposition = compositionDao.findById(compositionId);
        if (savedComposition == null) throw ExceptionSupplier.compositionNotFound(compositionId);

        Set<TrackClip> trackClips = payload.tracks().stream()
                .map(p -> trackClipMapper.toEntity(p, audioTrackDao.findById(p.audioTrackId()), savedComposition))
                .collect(Collectors.toSet());
        trackClips.forEach(trackClip -> {
            if (trackClip.isNew()) {
                trackClipDao.save(trackClip);
            } else {
                trackClipDao.update(trackClip);
            }
        });
        Composition entity = compositionMapper.toEntity(payload, trackClips, savedComposition);
        compositionDao.update(entity);
        return compositionMapper.toInfo(entity);
    }

    public void delete(Long id) { compositionDao.delete(id); }

    private Composition fetchById(Long id) {
        Composition result = compositionDao.findById(id);
        if (result == null) throw ExceptionSupplier.compositionNotFound(id);
        return result;
    }

    private List<Composition> fetchAll() { return compositionDao.findAll(); }
}