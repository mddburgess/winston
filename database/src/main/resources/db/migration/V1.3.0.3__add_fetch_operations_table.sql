CREATE TABLE fetch_operations
(
    id               BIGSERIAL,
    fetch_request_id BIGINT NOT NULL,
    operation_type   VARCHAR NOT NULL,
    object_id        VARCHAR NOT NULL,
    mode             VARCHAR,
    published_after  TIMESTAMP WITH TIME ZONE,
    published_before TIMESTAMP WITH TIME ZONE,
    status           VARCHAR NOT NULL,
    error            VARCHAR,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fetch_operations_pk PRIMARY KEY (id)
);

INSERT INTO fetch_operations (id, fetch_request_id, operation_type, object_id, mode, published_after, published_before,
                              status, error, created_at, last_updated_at)
SELECT id,
       id,
       fetch_type,
       object_id,
       mode,
       published_after,
       published_before,
       status,
       error,
       created_at,
       last_updated_at
FROM fetch_requests;

ALTER TABLE fetch_actions
    DROP CONSTRAINT fetch_actions_fk_fetch_request_id;

ALTER TABLE fetch_actions
    RENAME COLUMN fetch_request_id TO fetch_operation_id;

ALTER TABLE fetch_actions
    ADD CONSTRAINT fetch_actions_fk_fetch_operation_id
        FOREIGN KEY (fetch_operation_id) REFERENCES fetch_operations (id);

UPDATE fetch_actions
SET status = 'SUCCESSFUL'
WHERE status = 'COMPLETED';

UPDATE fetch_operations
SET status = 'SUCCESSFUL'
WHERE status = 'COMPLETED';

UPDATE fetch_requests
SET status = 'COMPLETED'
WHERE status = 'FAILED';
