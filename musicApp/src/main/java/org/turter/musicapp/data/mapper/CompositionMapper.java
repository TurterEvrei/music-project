package org.turter.musicapp.data.mapper;

import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.TrackClipDto;
import org.turter.musicapp.data.dto.payload.CompositionPayload;
import org.turter.musicapp.data.dto.payload.NewCompositionPayload;
import org.turter.musicapp.data.dto.payload.NewTrackClipPayload;
import org.turter.musicapp.data.dto.payload.TrackClipPayload;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.utils.AudioUtils;
import org.turter.musicapp.domain.AudioTrack;
import org.turter.musicapp.domain.Composition;
import org.turter.musicapp.domain.TrackClip;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositionMapper {

    public static AudioTrack toAudioTrack(Composition composition) {
        long durationMs = 0;
        String resultFilePath = composition.getResultFilePath();
        File file = new File(resultFilePath);
        if (file.exists() && file.isFile() && file.getName().endsWith(".mp3")) {
            try {
                durationMs = AudioUtils.getDurationMs(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AudioTrack(resultFilePath, composition.getTitle(), durationMs);
    }

    public static Composition toComposition(CompositionInfoDto compositionInfo) {
        return Composition.builder()
                .id(compositionInfo.id())
                .title(compositionInfo.title())
                .resultFilePath("")
                .tracks(compositionInfo.tracks().stream().flatMap(CompositionMapper::toTrackClip)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Stream<TrackClip> toTrackClip(TrackClipDto dto) {
        return TrackCacheStore.getCachedTrack(dto.audioTrack().id().toString()).stream().map(track -> new TrackClip(
                track,
                dto.startTimeMs(),
                dto.durationMs(),
                dto.volume()
        ));
    }

    public static CompositionPayload toPayload(Composition composition) {
        return new CompositionPayload(
                composition.getId(),
                composition.getTitle(),
                CompositionMapper.toPayloadSet(composition.getTracks())
        );
    }

    private static TrackClipPayload toPayload(TrackClip trackClip) {
        return new TrackClipPayload(trackClip.getRemoteId(), trackClip.getStartTimeMs(), trackClip.getVolume(),
                trackClip.getDurationMs(), trackClip.getAudioTrack().getRemoteId());
    }

    private static Set<TrackClipPayload> toPayloadSet(List<TrackClip> trackClips) {
        return trackClips.stream().map(CompositionMapper::toPayload).collect(Collectors.toSet());
    }

    public static NewCompositionPayload toNewPayload(Composition composition) {
        return new NewCompositionPayload(
                composition.getTitle(),
                CompositionMapper.toNewPayloadSet(composition.getTracks())
        );
    }

    private static NewTrackClipPayload toNewPayload(TrackClip trackClip) {
        return new NewTrackClipPayload(trackClip.getStartTimeMs(), trackClip.getVolume(),
                trackClip.getDurationMs(), trackClip.getAudioTrack().getRemoteId());
    }

    private static Set<NewTrackClipPayload> toNewPayloadSet(List<TrackClip> trackClips) {
        return trackClips.stream().map(CompositionMapper::toNewPayload).collect(Collectors.toSet());
    }

}
