ALTER TABLE channels
    ADD COLUMN uploads_playlist_id VARCHAR,
    ADD COLUMN video_count         BIGINT,
    ADD COLUMN view_count          BIGINT,
    ADD COLUMN subscriber_count    BIGINT;
