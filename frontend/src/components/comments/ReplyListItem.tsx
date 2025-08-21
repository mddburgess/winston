import { ListGroupItem } from "react-bootstrap";
import { CommentDisplayRow } from "#/components/comments/CommentDisplayRow";
import type { Comment } from "#/api";

type ReplyListItemProps = {
    reply: Comment;
    highlightAuthorId?: string;
};

export const ReplyListItem = ({
    reply,
    highlightAuthorId,
}: ReplyListItemProps) => (
    <ListGroupItem key={reply.id}>
        <CommentDisplayRow
            comment={reply}
            highlightAuthorId={highlightAuthorId}
            isReply={true}
        />
    </ListGroupItem>
);
