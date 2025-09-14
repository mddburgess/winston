ALTER TABLE comments
    DROP CONSTRAINT comments_fk_author_id,
    DROP CONSTRAINT comments_fk_parent_id,
    DROP CONSTRAINT comments_fk_video_id;
