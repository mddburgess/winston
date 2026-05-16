ALTER TABLE fetch_operations
    ALTER COLUMN fetch_request_id DROP NOT NULL,
    ADD CONSTRAINT fetch_operations_fk_fetch_request_id
        FOREIGN KEY (fetch_request_id) REFERENCES fetch_requests (id);
