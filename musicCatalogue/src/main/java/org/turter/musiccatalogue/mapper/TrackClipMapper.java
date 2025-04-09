package org.turter.musiccatalogue.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.turter.musiccatalogue.dto.TrackClipDto;
import org.turter.musiccatalogue.dto.payload.NewTrackClipPayload;
import org.turter.musiccatalogue.dto.payload.TrackClipPayload;
import org.turter.musiccatalogue.entity.AudioTrack;
import org.turter.musiccatalogue.entity.Composition;
import org.turter.musiccatalogue.entity.TrackClip;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AudioTrackMapper.class})
public interface TrackClipMapper {

    TrackClipDto toDto(TrackClip trackClip);

    List<TrackClipDto> toDtoList(List<TrackClip> trackClips);

    @Mapping(target = "id", ignore = true)
    TrackClip toNewEntity(NewTrackClipPayload payload, AudioTrack audioTrack);

    @Mapping(target = "id", source = "payload.id")
    TrackClip toEntity(TrackClipPayload payload, AudioTrack audioTrack, Composition composition);

}
