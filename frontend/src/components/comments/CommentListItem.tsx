import { ListGroupItem, Row } from "react-bootstrap";
import { CommentDisplayRow } from "#/components/comments/CommentDisplayRow";
import { selectAllReplies } from "#/store/slices/comments";
import { ReplyList } from "./ReplyList";
import type { CommentState } from "#/store/slices/backend";

type CommentListItemProps = {
  comment: CommentState;
  focusAuthor?: string;
};

export const CommentListItem = ({ comment, focusAuthor }: CommentListItemProps) => (
  <ListGroupItem key={comment.id}>
    <CommentDisplayRow comment={comment} focusAuthor={focusAuthor} />
    <Row>
      <ReplyList
        commentId={comment.id}
        totalReplyCount={comment.total_reply_count}
        replies={selectAllReplies(comment.replies)}
        focusAuthor={focusAuthor}
      />
    </Row>
  </ListGroupItem>
);
