ALTER TABLE video_topics
    DROP CONSTRAINT video_topics_pk,
    ADD CONSTRAINT video_topics_pk PRIMARY KEY (video_id, topic_url);

ALTER TABLE video_tags
    DROP CONSTRAINT video_tags_pk,
    ADD CONSTRAINT video_tags_pk PRIMARY KEY (video_id, tag);
