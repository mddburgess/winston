import { ListGroupItem, Row } from "react-bootstrap";
import { CommentDisplayRow } from "#/components/comments/CommentDisplayRow";
import { selectAllReplies } from "#/store/slices/comments";
import { ReplyList } from "./ReplyList";
import type { CommentState } from "#/store/slices/backend";

type CommentListItemProps = {
    comment: CommentState;
    highlightAuthorId?: string;
};

export const CommentListItem = ({
    comment,
    highlightAuthorId,
}: CommentListItemProps) => (
    <ListGroupItem key={comment.id}>
        <CommentDisplayRow
            comment={comment}
            highlightAuthorId={highlightAuthorId}
        />
        <Row>
            <ReplyList
                commentId={comment.id}
                totalReplyCount={comment.total_reply_count}
                replies={selectAllReplies(comment.replies)}
                highlightAuthorId={highlightAuthorId}
            />
        </Row>
    </ListGroupItem>
);
