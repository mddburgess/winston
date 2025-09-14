import { ListGroup } from "react-bootstrap";
import { PullRepliesActionItem } from "./PullRepliesActionItem";
import { ReplyListItem } from "./ReplyListItem";
import type { Comment } from "#/api";

type ReplyListProps = {
  commentId: string;
  totalReplyCount: number;
  replies: Comment[];
  highlightAuthorId?: string;
};

const ReplyList = ({ highlightAuthorId = "", ...props }: ReplyListProps) => (
  <ListGroup variant={"flush"} className={"ps-4"}>
    {props.replies.map((reply) => (
      <ReplyListItem key={reply.id} reply={reply} highlightAuthorId={highlightAuthorId} />
    ))}
    <PullRepliesActionItem {...props} />
  </ListGroup>
);

export { ReplyList };
