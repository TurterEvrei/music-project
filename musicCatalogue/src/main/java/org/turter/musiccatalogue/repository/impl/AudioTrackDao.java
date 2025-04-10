package org.turter.musiccatalogue.repository.impl;

import org.springframework.stereotype.Repository;
import org.turter.musiccatalogue.entity.AudioTrack;

@Repository
//@Transactional
public class AudioTrackDao extends GenericDaoImpl<AudioTrack, Long> {
    public AudioTrackDao() { super(AudioTrack.class); }
}