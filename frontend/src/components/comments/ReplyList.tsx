import {ListGroup} from "react-bootstrap";
import {ReplyListItem} from "./ReplyListItem";
import {CommentDto} from "../../model/CommentDto";

type ReplyListProps = {
    replies: CommentDto[]
}

export const ReplyList = ({ replies }: ReplyListProps) => (
    <ListGroup>
        {replies.map((reply) => (<ReplyListItem reply={reply}/>))}
    </ListGroup>
)
