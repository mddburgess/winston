CREATE TABLE channels
(
    id              VARCHAR NOT NULL,
    title           VARCHAR,
    description     VARCHAR,
    custom_url      VARCHAR,
    thumbnail_url   VARCHAR,
    published_at    TIMESTAMP(6) WITH TIME ZONE,
    last_fetched_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT channels_pk PRIMARY KEY (id)
);

CREATE TABLE channel_topics
(
    channel_id VARCHAR NOT NULL,
    topic_url  VARCHAR NOT NULL,
    CONSTRAINT channel_topics_pk PRIMARY KEY (channel_id, topic_url),
    CONSTRAINT channel_topics_fk_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id)
);

CREATE TABLE channel_keywords
(
    channel_id VARCHAR NOT NULL,
    keyword    VARCHAR NOT NULL,
    CONSTRAINT channel_keywords_pk PRIMARY KEY (channel_id, keyword),
    CONSTRAINT channel_keywords_fk_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id)
);
