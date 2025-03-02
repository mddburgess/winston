CREATE TABLE thumbnails
(
    id    VARCHAR NOT NULL,
    url   VARCHAR NOT NULL,
    image BYTEA NOT NULL,
    CONSTRAINT thumbnails_pk PRIMARY KEY (id)
);
