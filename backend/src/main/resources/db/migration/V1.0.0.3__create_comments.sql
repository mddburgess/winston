CREATE TABLE authors
(
    id                VARCHAR NOT NULL,
    display_name      VARCHAR,
    channel_url       VARCHAR,
    profile_image_url VARCHAR,
    last_fetched_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT authors_pk PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id              VARCHAR NOT NULL,
    video_id        VARCHAR,
    parent_id       VARCHAR,
    author_id       VARCHAR,
    text_display    VARCHAR,
    text_original   VARCHAR,
    reply_count     BIGINT,
    published_at    TIMESTAMP(6) WITH TIME ZONE,
    updated_at      TIMESTAMP(6) WITH TIME ZONE,
    last_fetched_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT comments_pk PRIMARY KEY (id),
    CONSTRAINT comments_fk_video_id FOREIGN KEY (video_id) REFERENCES videos (id),
    CONSTRAINT comments_fk_parent_id FOREIGN KEY (parent_id) REFERENCES comments (id),
    CONSTRAINT comments_fk_author_id FOREIGN KEY (author_id) REFERENCES authors (id)
);
