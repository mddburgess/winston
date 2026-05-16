CREATE TABLE comment_properties
(
    comment_id VARCHAR NOT NULL,
    important  BOOLEAN NOT NULL DEFAULT FALSE,
    hidden     BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT comment_properties_pk PRIMARY KEY (comment_id),
    CONSTRAINT comment_properties_fk_comment_id FOREIGN KEY (comment_id) REFERENCES comments (id)
);
