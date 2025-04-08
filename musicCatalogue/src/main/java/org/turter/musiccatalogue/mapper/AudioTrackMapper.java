package org.turter.musiccatalogue.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.turter.musiccatalogue.dto.AudioTrackDto;
import org.turter.musiccatalogue.dto.payload.AudioTrackPayload;
import org.turter.musiccatalogue.dto.payload.NewAudioTrackPayload;
import org.turter.musiccatalogue.entity.AudioTrack;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AudioTrackMapper {

    AudioTrackDto toDto(AudioTrack audioTrack);

    List<AudioTrackDto> toDtoList(List<AudioTrack> audioTracks);

    @Mapping(target = "id", ignore = true)
    AudioTrack toNewEntity(NewAudioTrackPayload payload);

    AudioTrack toEntity(AudioTrackPayload payload);

}
