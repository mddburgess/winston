import {ListGroup} from "react-bootstrap";
import {CommentState} from "../../store/slices/comments";
import {CommentListItem} from "./CommentListItem";

type CommentListProps = {
    comments?: CommentState[],
    highlightAuthorId?: string
}
export const CommentList = ({comments = [], highlightAuthorId = ""}: CommentListProps) => (
    <ListGroup className={"pb-3 pt-2"}>
        {comments.map((comment) => (
            <CommentListItem key={comment.id} comment={comment} highlightAuthorId={highlightAuthorId}/>))
        }
    </ListGroup>
)
