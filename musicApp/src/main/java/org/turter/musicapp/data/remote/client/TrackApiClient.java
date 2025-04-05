package org.turter.musicapp.data.remote.client;

import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.NewAudioTrackDto;

import java.util.List;

public interface TrackApiClient {
    List<AudioTrackDto> getAllTracks();
    AudioTrackDto createTrack(NewAudioTrackDto payload);
    boolean deleteTrack(long trackId);
}
