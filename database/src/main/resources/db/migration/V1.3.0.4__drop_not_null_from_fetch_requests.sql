ALTER TABLE fetch_requests
    ALTER COLUMN fetch_type DROP NOT NULL,
    ALTER COLUMN object_id DROP NOT NULL;
