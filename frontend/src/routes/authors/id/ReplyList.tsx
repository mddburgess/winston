import {CommentDto} from "../../../model/CommentDto";
import {ListGroup} from "react-bootstrap";
import {ReplyListItem} from "./ReplyListItem";

type ReplyListProps = {
    replies?: CommentDto[]
}

export const ReplyList = ({ replies = [] }: ReplyListProps) => (
    <ListGroup>
        {replies.map((reply) => (<ReplyListItem reply={reply}/>))}
    </ListGroup>
)
