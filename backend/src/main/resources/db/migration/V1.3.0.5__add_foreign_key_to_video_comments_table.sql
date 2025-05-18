ALTER TABLE video_comments
    ADD CONSTRAINT video_comments_fk_video_id
        FOREIGN KEY (video_id) REFERENCES videos (id);
