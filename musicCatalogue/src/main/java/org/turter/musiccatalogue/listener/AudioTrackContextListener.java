package org.turter.musiccatalogue.listener;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.turter.musiccatalogue.entity.AudioTrack;
import org.turter.musiccatalogue.repository.impl.AudioTrackDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AudioTrackContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private final AudioTrackDao audioTrackDao;

    private final List<Pair<Long, String>> initAudioTracks = List.of(
            Pair.of(1L, "inecraft_death.mp3"),
            Pair.of(2L, "inecraft_eating.mp3"),
            Pair.of(3L, "inecraft_hit_sound.mp3")
            );

    private boolean initialized = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initialized) return;
        initialized = true;

        for (Pair<Long, String> pair : initAudioTracks) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("audio/" + pair.getRight())) {
                if (is == null) {
                    System.err.println("Файл не найден: " + pair.getRight());
                    continue;
                }

                byte[] data = is.readAllBytes();

                AudioTrack track = new AudioTrack();
                track.setId(pair.getLeft());
                track.setTitle(pair.getRight());
                track.setData(data);

                audioTrackDao.update(track);
                System.out.println("Добавлен трек: " + pair.getRight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
