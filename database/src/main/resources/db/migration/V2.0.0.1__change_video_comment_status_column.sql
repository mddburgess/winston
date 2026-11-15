ALTER TABLE video_comments
ADD COLUMN comment_status VARCHAR NOT NULL DEFAULT 'ENABLED';

UPDATE video_comments
SET comment_status = 'DISABLED'
WHERE comments_disabled = true;

ALTER TABLE video_comments
DROP COLUMN comments_disabled;
