import { CommentCounts } from "#/components/comments/CommentCounts";
import type { VideoProps } from "#/types";

type VideoCommentCountsProps = VideoProps & {
  showTotalReplyCount?: boolean;
};

const VideoCommentCounts = ({ video, showTotalReplyCount }: VideoCommentCountsProps) => (
  <CommentCounts
    commentsDisabled={video.comments?.comments_disabled}
    commentCount={Math.max(video.details?.comment_count ?? 0, video.comments?.comment_count ?? 0)}
    replyCount={video.comments?.reply_count}
    totalReplyCount={video.comments?.total_reply_count}
    lastFetchedAt={video.comments?.last_fetched_at}
    showTotalReplyCount={showTotalReplyCount}
  />
);

export { VideoCommentCounts };
