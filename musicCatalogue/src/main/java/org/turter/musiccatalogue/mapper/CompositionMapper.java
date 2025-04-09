package org.turter.musiccatalogue.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.turter.musiccatalogue.dto.CompositionInfo;
import org.turter.musiccatalogue.dto.CompositionPreview;
import org.turter.musiccatalogue.dto.payload.CompositionPayload;
import org.turter.musiccatalogue.entity.Composition;
import org.turter.musiccatalogue.entity.TrackClip;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {TrackClipMapper.class})
public interface CompositionMapper {

    CompositionInfo toInfo(Composition composition);

    List<CompositionInfo> toInfos(List<Composition> compositions);

    CompositionPreview toPreview(Composition composition);

    List<CompositionPreview> toPreviews(List<Composition> compositions);

    Composition toNewEntity(String title, Set<TrackClip> tracks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tracks", source = "tracks")
    Composition toEntity(CompositionPayload payload, Set<TrackClip> tracks, @MappingTarget Composition composition);

}
