CREATE TABLE video_details
(
    video_id                   VARCHAR NOT NULL,
    visibility                 VARCHAR,
    duration                   NUMERIC,
    category                   VARCHAR,
    made_for_kids              BOOLEAN,
    contains_synthetic_media   BOOLEAN,
    has_paid_product_placement BOOLEAN,
    recorded_at                TIMESTAMP WITH TIME ZONE,
    live_streamed_at           TIMESTAMP WITH TIME ZONE,
    view_count                 BIGINT,
    like_count                 BIGINT,
    comment_count              BIGINT,
    CONSTRAINT video_details_pk PRIMARY KEY (video_id)
);

CREATE TABLE video_topics
(
    video_id  VARCHAR NOT NULL,
    topic_url VARCHAR NOT NULL,
    CONSTRAINT video_topics_pk PRIMARY KEY (video_id, topic_url)
);

CREATE TABLE video_tags
(
    video_id VARCHAR NOT NULL,
    tag      VARCHAR NOT NULL,
    CONSTRAINT video_tags_pk PRIMARY KEY (video_id, tag)
);

CREATE TABLE video_restrictions
(
    id          BIGSERIAL,
    video_id    VARCHAR,
    restriction VARCHAR NOT NULL,
    country     VARCHAR NOT NULL,
    CONSTRAINT video_restrictions_pk PRIMARY KEY (id)
);

CREATE TABLE video_content_ratings
(
    id        BIGSERIAL,
    video_id  VARCHAR,
    authority VARCHAR NOT NULL,
    rating    VARCHAR NOT NULL,
    CONSTRAINT video_content_ratings_pk PRIMARY KEY (id)
);

CREATE TABLE video_recording_locations
(
    video_id    VARCHAR NOT NULL,
    description VARCHAR,
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    altitude    DOUBLE PRECISION,
    CONSTRAINT video_recording_locations_pk PRIMARY KEY (video_id)
);
