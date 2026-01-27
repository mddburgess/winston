CREATE TABLE author_aliases
(
    author_id    VARCHAR NOT NULL,
    author_alias VARCHAR NOT NULL,
    CONSTRAINT author_aliases_pk PRIMARY KEY (author_id, author_alias),
    CONSTRAINT author_aliases_fk_author_id FOREIGN KEY (author_id) REFERENCES authors (id)
);
