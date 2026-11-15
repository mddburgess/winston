import { Col } from "react-bootstrap";
import { Chat, ChatFill, ChatQuote, ChatQuoteFill } from "react-bootstrap-icons";
import { formatInteger } from "#/utils/formatInteger";
import type { CommentStatus } from "#/api";

type CommentCountsProps = {
  commentStatus?: CommentStatus;
  commentCount?: number;
  replyCount?: number;
  totalReplyCount?: number;
  lastFetchedAt?: string;
  showTotalReplyCount?: boolean;
};

type CommentsColProps = Required<Pick<CommentCountsProps, "commentCount" | "commentStatus">> & {
  hasBeenFetched: boolean;
};

type RepliesColProps = Required<Pick<CommentCountsProps, "replyCount" | "totalReplyCount" | "showTotalReplyCount">>;

const CommentCounts = ({
  commentStatus = "enabled",
  commentCount = 0,
  replyCount = 0,
  totalReplyCount = 0,
  lastFetchedAt,
  showTotalReplyCount = true,
}: CommentCountsProps) => {
  const hasBeenFetched = lastFetchedAt !== undefined;
  return (
    <>
      <CommentsCol commentCount={commentCount} commentStatus={commentStatus} hasBeenFetched={hasBeenFetched} />
      {hasBeenFetched && commentStatus === "enabled" && (
        <RepliesCol
          replyCount={replyCount}
          totalReplyCount={totalReplyCount}
          showTotalReplyCount={showTotalReplyCount}
        />
      )}
    </>
  );
};

const CommentsCol = ({ commentCount, commentStatus, hasBeenFetched }: CommentsColProps) => {
  const CommentsIcon = hasBeenFetched ? ChatFill : Chat;
  return (
    <Col
      className={"align-items-center d-flex" + (commentStatus === "enabled" ? "" : " text-body-tertiary")}
      data-testid={"comments"}
      xs={"auto"}
    >
      <CommentsIcon className={"me-2"} data-testid={"commentsIcon"} />
      {commentStatus === "disabled"
        ? "comments disabled"
        : commentStatus === "restricted"
          ? "comments restricted"
          : formatInteger(commentCount)}
    </Col>
  );
};

const RepliesCol = ({ replyCount, totalReplyCount, showTotalReplyCount }: RepliesColProps) => {
  const RepliesIcon = replyCount >= totalReplyCount ? ChatQuoteFill : ChatQuote;
  return (
    <Col className={"align-items-center d-flex"} data-testid={"replies"} xs={"auto"}>
      <RepliesIcon className={"me-2"} data-testid={"repliesIcon"} />
      {formatInteger(replyCount)}
      {showTotalReplyCount && replyCount < totalReplyCount && (
        <span className={"text-body-tertiary"} data-testid="totalReplies">
          &nbsp;/ {formatInteger(totalReplyCount)}
        </span>
      )}
    </Col>
  );
};

export { CommentCounts };
