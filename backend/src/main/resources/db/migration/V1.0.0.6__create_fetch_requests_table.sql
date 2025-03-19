CREATE TABLE fetch_requests
(
    id              BIGSERIAL,
    fetch_id        VARCHAR NOT NULL,
    status          VARCHAR NOT NULL,
    error           VARCHAR,
    requested_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fetch_requests_pk PRIMARY KEY (id)
);
