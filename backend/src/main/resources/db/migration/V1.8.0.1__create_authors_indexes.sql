CREATE INDEX IF NOT EXISTS authors_idx_display_name ON authors (display_name);
CREATE INDEX IF NOT EXISTS comments_idx_author_id ON comments (author_id);
