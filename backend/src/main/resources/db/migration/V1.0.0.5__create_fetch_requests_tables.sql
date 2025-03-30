CREATE TABLE fetch_requests
(
    id               BIGSERIAL,
    fetch_type       VARCHAR NOT NULL,
    object_id        VARCHAR NOT NULL,
    mode             VARCHAR,
    published_after  TIMESTAMP WITH TIME ZONE,
    published_before TIMESTAMP WITH TIME ZONE,
    status           VARCHAR NOT NULL,
    error            VARCHAR,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fetch_requests_pk PRIMARY KEY (id)
);

CREATE TABLE fetch_actions
(
    id               BIGSERIAL,
    fetch_request_id BIGINT NOT NULL,
    action_type      VARCHAR NOT NULL,
    object_id        VARCHAR NOT NULL,
    published_after  TIMESTAMP WITH TIME ZONE,
    published_before TIMESTAMP WITH TIME ZONE,
    page_token       VARCHAR,
    status           VARCHAR NOT NULL,
    item_count       INTEGER,
    error            VARCHAR,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fetch_actions_pk PRIMARY KEY (id),
    CONSTRAINT fetch_actions_fk_fetch_request_id FOREIGN KEY (fetch_request_id) REFERENCES fetch_requests (id)
);

CREATE TABLE youtube_requests
(
    id               BIGSERIAL,
    fetch_action_id  BIGINT NOT NULL,
    request_type     VARCHAR NOT NULL,
    object_id        VARCHAR NOT NULL,
    published_after  VARCHAR,
    published_before VARCHAR,
    page_token       VARCHAR,
    requested_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    http_status      INTEGER,
    item_count       INTEGER,
    error            VARCHAR,
    responded_at     TIMESTAMP WITH TIME ZONE,
    CONSTRAINT youtube_requests_pk PRIMARY KEY (id),
    CONSTRAINT youtube_requests_fk_fetch_action_id FOREIGN KEY (fetch_action_id) REFERENCES fetch_actions (id)
);
