import {ListGroup} from "react-bootstrap";
import {CommentDto} from "../../model/CommentDto";
import {CommentListItem} from "./CommentListItem";

type CommentListProps = {
    comments?: CommentDto[]
}
export const CommentList = ({comments = []}: CommentListProps) => (
    <ListGroup>
        {comments.map((comment: CommentDto) => (<CommentListItem comment={comment}/>))}
    </ListGroup>
)
