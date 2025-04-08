CREATE TABLE IF NOT EXISTS audio_tracks (
                              id BIGSERIAL PRIMARY KEY,
                              title VARCHAR(255) UNIQUE NOT NULL,
                              data bytea
);

CREATE TABLE IF NOT EXISTS compositions (
                              id BIGSERIAL PRIMARY KEY,
                              title VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS track_clips (
                             id BIGSERIAL PRIMARY KEY,
                             start_time_ms BIGINT NOT NULL DEFAULT 0,
                             volume DOUBLE PRECISION NOT NULL DEFAULT 1.0,
                             duration_ms BIGINT NOT NULL,
                             audio_track_id BIGINT,
                             composition_id BIGINT,
                             FOREIGN KEY (audio_track_id) REFERENCES audio_tracks(id) ON DELETE CASCADE ,
                             FOREIGN KEY (composition_id) REFERENCES compositions(id) ON DELETE CASCADE
);