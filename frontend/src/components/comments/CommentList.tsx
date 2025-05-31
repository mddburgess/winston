import { ListGroup } from "react-bootstrap";
import { CommentListItem } from "./CommentListItem";
import type { TopLevelComment } from "#/store/slices/backend";

type Props = {
    comments?: TopLevelComment[];
    highlightAuthorId?: string;
};

export const CommentList = ({
    comments = [],
    highlightAuthorId = "",
}: Props) => (
    <ListGroup className={"pb-3 pt-2"}>
        {comments.map((comment) => (
            <CommentListItem
                key={comment.id}
                comment={comment}
                highlightAuthorId={highlightAuthorId}
            />
        ))}
    </ListGroup>
);
