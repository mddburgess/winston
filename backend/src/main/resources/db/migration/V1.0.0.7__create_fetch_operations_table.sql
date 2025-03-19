CREATE TABLE fetch_operations
(
    id                BIGSERIAL,
    fetch_request_id  BIGINT NOT NULL,
    channel_id        VARCHAR NOT NULL,
    last_published_at TIMESTAMP WITH TIME ZONE,
    next_page_token   VARCHAR,
    status            VARCHAR NOT NULL,
    error             VARCHAR,
    requested_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at   timestamp WITH TIME ZONE NOT NULL,
    CONSTRAINT fetch_operations_pk PRIMARY KEY (id),
    CONSTRAINT fetch_operations_fk_fetch_request_id FOREIGN KEY (fetch_request_id) REFERENCES fetch_requests (id)
);
