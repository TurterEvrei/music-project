package org.turter.dao;

import org.turter.entity.AudioTrack;
import org.turter.servlet.payload.NewAudioTrackPayload;

import java.util.List;

public interface AudioTrackDao {
    AudioTrack getAudioTrackById(Long id);
    List<AudioTrack> getAllAudioTracks();
    AudioTrack saveAudioTrack(NewAudioTrackPayload payload);
    AudioTrack updateAudioTrack(AudioTrack audioTrack);
    void deleteAudioTrack(Long id);
}
