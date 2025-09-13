ALTER TABLE video_details
    DROP CONSTRAINT video_details_fk_video_id;

ALTER TABLE video_topics
    DROP CONSTRAINT video_topics_fk_video_id;

ALTER TABLE video_tags
    DROP CONSTRAINT video_tags_fk_video_id;

ALTER TABLE video_restrictions
    DROP CONSTRAINT video_restrictions_fk_video_id;

ALTER TABLE video_content_ratings
    DROP CONSTRAINT video_content_ratings_fk_video_id;

ALTER TABLE video_recording_locations
    DROP CONSTRAINT video_recording_locations_fk_video_id;
