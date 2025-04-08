INSERT INTO audio_tracks (title, data)
VALUES
    ('Трек 1', NULL),
    ('Трек 2', NULL),
    ('Трек 3', NULL),
    ('Трек 4', NULL);

INSERT INTO compositions (title)
VALUES
    ('Моя первая композиция'),
    ('Вторая композиция');

INSERT INTO track_clips (
    start_time_ms,
    volume,
    duration_ms,
    audio_track_id,
    composition_id
)
VALUES
(0, 1.0, 3000, 1, 1),
(500, 0.8, 2500, 2, 1),
(1000, 0.9, 2000, 1, 1),

(0, 1.0, 4000, 3, 2),
(200, 0.7, 3500, 4, 2),
(1500, 0.6, 3000, 3, 2);