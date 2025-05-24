CREATE TABLE video_comments
(
    video_id          VARCHAR NOT NULL,
    comments_disabled BOOLEAN NOT NULL DEFAULT FALSE,
    comment_count     BIGINT NOT NULL DEFAULT 0,
    reply_count       BIGINT NOT NULL DEFAULT 0,
    total_reply_count BIGINT NOT NULL DEFAULT 0,
    last_fetched_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (video_id)
);

INSERT INTO video_comments
SELECT v.id                             AS video_id,
       v.comments_disabled              AS comments_disabled,
       COUNT(c.id) - COUNT(c.parent_id) AS comment_count,
       COUNT(c.parent_id)               AS reply_count,
       SUM(c.total_reply_count)         AS total_reply_count,
       MAX(fr.last_fetched_at)          AS last_fetched_at
FROM videos AS v
         JOIN comments AS c ON v.id = c.video_id
         JOIN (SELECT object_id, MAX(last_updated_at) AS last_fetched_at
               FROM fetch_requests
               WHERE fetch_type = 'COMMENTS'
               GROUP BY object_id) AS fr ON v.id = fr.object_id
GROUP BY v.id;
