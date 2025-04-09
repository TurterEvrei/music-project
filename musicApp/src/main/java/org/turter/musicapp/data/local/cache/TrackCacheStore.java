package org.turter.musicapp.data.local.cache;

import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.utils.AudioUtils;
import org.turter.musicapp.domain.AudioTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrackCacheStore {
    private static final String CACHE_DIR = "musicApp/src/main/resources/cache/tracks/";
    private static final ConcurrentMap<String, AudioTrack> STORE = new ConcurrentHashMap<>();

    static {
        // Создаем папку, если её нет
        File dir = new File(CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println(dir.getAbsolutePath());
    }

    public static AudioTrack saveTrack(AudioTrackDto trackDto) {
        return TrackCacheStore.saveTrack(trackDto.id(), trackDto.title(), trackDto.data());
    }

    /**
     * Сохраняет один трек в виде MP3-файла
     */
    public static AudioTrack saveTrack(Long remoteId, String title, byte[] data) {
        File file = new File(CACHE_DIR + sanitizeFileName(title));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.flush();

            AudioTrack audioTrack = new AudioTrack(remoteId, file.getAbsolutePath(), title,
                    AudioUtils.getDurationMs(file.getAbsolutePath()));
            STORE.put(remoteId.toString(), audioTrack);
            return audioTrack;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Сохраняет список треков
     */
    public static void saveTracks(List<AudioTrackDto> trackDtos) {
        for (AudioTrackDto trackDto : trackDtos) {
            saveTrack(trackDto);
        }
    }

    public static void removeTrack(String id) {
        AudioTrack target = STORE.get(id);
        if (target != null) {
            STORE.remove(id);
            new File(target.getFilePath()).delete();
        }
    }

    /**
     * Возвращает список всех MP3-файлов в кэше в виде объектов AudioTrack
     */
    public static List<AudioTrack> getCachedTracks() {
        return STORE.values().stream().sorted(Comparator.comparing(AudioTrack::getName)).toList();
    }

    /**
     * Возвращает AudioTrack по id
     */
    public static Optional<AudioTrack> getCachedTrack(String id) {
        return Optional.ofNullable(STORE.get(id));
    }

    /**
     * Выявляет пуст ли кэш
     */
    public static boolean isEmpty() {
        return STORE.isEmpty();
    }

    /**
     * Удаляет все файлы MP3 из кэша
     */
    public static void evictCache() {
        STORE.clear();
        File[] files = new File(CACHE_DIR).listFiles();
        for (File file : files) file.delete();
    }

    /**
     * Очистка имени файла от недопустимых символов
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}


