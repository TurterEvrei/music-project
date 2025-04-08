package org.turter.musiccatalogue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.turter.musiccatalogue.dto.AudioTrackDto;
import org.turter.musiccatalogue.dto.payload.AudioTrackPayload;
import org.turter.musiccatalogue.dto.payload.NewAudioTrackPayload;
import org.turter.musiccatalogue.entity.AudioTrack;
import org.turter.musiccatalogue.mapper.AudioTrackMapper;
import org.turter.musiccatalogue.repository.impl.AudioTrackDao;
import org.turter.musiccatalogue.util.ExceptionSupplier;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AudioTrackService {
    private final AudioTrackDao audioTrackDao;
    private final AudioTrackMapper mapper;

    public AudioTrackDto get(Long id) {
        AudioTrack result = audioTrackDao.findById(id);
        if (result == null) throw ExceptionSupplier.audioTrackNotFound(id);
        return mapper.toDto(result);
    }

    public List<AudioTrackDto> getAll() {
        return mapper.toDtoList(audioTrackDao.findAll());
    }

    public AudioTrackDto save(NewAudioTrackPayload payload) {
        AudioTrack newEntity = mapper.toNewEntity(payload);
        audioTrackDao.save(newEntity);
        return mapper.toDto(newEntity);
    }

    public AudioTrackDto update(AudioTrackPayload payload) {
        AudioTrack entity = mapper.toEntity(payload);
        audioTrackDao.update(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) { audioTrackDao.delete(id); }
}