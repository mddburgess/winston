import {ListGroup} from "react-bootstrap";
import {CommentDto} from "../../model/CommentDto";
import {CommentListItem} from "./CommentListItem";

type CommentListProps = {
    comments?: CommentDto[],
    highlightAuthorId?: string
}
export const CommentList = ({comments = [], highlightAuthorId = ""}: CommentListProps) => (
    <ListGroup className={"pb-3 pt-2"}>
        {comments.map((comment: CommentDto) => (
            <CommentListItem key={comment.id} comment={comment} highlightAuthorId={highlightAuthorId}/>))
        }
    </ListGroup>
)
