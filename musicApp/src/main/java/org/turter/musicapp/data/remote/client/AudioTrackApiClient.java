package org.turter.musicapp.data.remote.client;

import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.payload.AudioTrackPayload;
import org.turter.musicapp.data.dto.payload.NewAudioTrackPayload;

import java.util.List;

public interface AudioTrackApiClient {
    List<AudioTrackDto> getAllTracks();
    AudioTrackDto createTrack(NewAudioTrackPayload payload);
    AudioTrackDto updateTrack(AudioTrackPayload payload);
    boolean deleteTrack(long id);
}
