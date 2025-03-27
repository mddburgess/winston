import {ListGroup} from "react-bootstrap";
import {ReplyListItem} from "./ReplyListItem";
import {CommentDto} from "../../model/CommentDto";

type ReplyListProps = {
    replies: CommentDto[]
}

export const ReplyList = ({ replies }: ReplyListProps) => (
    <ListGroup variant={"flush"} className={"ps-4"}>
        {replies.map((reply) => (<ReplyListItem key={reply.id} reply={reply}/>))}
    </ListGroup>
)
