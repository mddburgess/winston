CREATE INDEX IF NOT EXISTS comments_idx_video_id ON comments (video_id);
CREATE INDEX IF NOT EXISTS comments_idx_parent_id ON comments (parent_id);
