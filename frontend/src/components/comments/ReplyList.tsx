import { ListGroup } from "react-bootstrap";
import { PullRepliesActionItem } from "./PullRepliesActionItem";
import { ReplyListItem } from "./ReplyListItem";
import type { Comment } from "#/api";

type ReplyListProps = {
  commentId: string;
  totalReplyCount: number;
  replies: Comment[];
  focusAuthor?: string;
};

const ReplyList = ({ focusAuthor, ...props }: ReplyListProps) => (
  <ListGroup variant={"flush"} className={"ps-4"}>
    {props.replies.map((reply) => (
      <ReplyListItem key={reply.id} reply={reply} focusAuthor={focusAuthor} />
    ))}
    <PullRepliesActionItem {...props} />
  </ListGroup>
);

export { ReplyList };
