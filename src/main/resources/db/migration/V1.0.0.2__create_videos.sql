CREATE TABLE videos
(
    id              VARCHAR NOT NULL,
    channel_id      VARCHAR NOT NULL,
    title           VARCHAR,
    description     VARCHAR,
    thumbnail_url   VARCHAR,
    published_at    TIMESTAMP(6) WITH TIME ZONE,
    last_fetched_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT videos_pk PRIMARY KEY (id),
    CONSTRAINT videos_fk_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id)
);
