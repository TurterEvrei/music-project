package org.turter.mapper;

import org.turter.entity.AudioTrack;
import org.turter.servlet.payload.NewAudioTrackPayload;

public class AudioTrackMapper {
    public static AudioTrack toEntity(NewAudioTrackPayload payload) {
        return new AudioTrack()
                .setTitle(payload.getTitle())
                .setData(payload.getData());
    }
}
