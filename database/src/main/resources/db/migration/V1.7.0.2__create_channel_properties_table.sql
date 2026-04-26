CREATE TABLE channel_properties
(
    channel_id VARCHAR NOT NULL,
    archived   BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT channel_properties_pk PRIMARY KEY (channel_id),
    CONSTRAINT channel_properties_fk_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id)
);
