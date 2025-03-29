import {ListGroup, ListGroupItem} from "react-bootstrap";
import {ReplyListItem} from "./ReplyListItem";
import {CommentDto} from "../../model/CommentDto";
import {pluralize} from "../../utils";
import {ReplyAll} from "react-bootstrap-icons";

type ReplyListProps = {
    totalReplyCount: number,
    replies: CommentDto[]
}

export const ReplyList = ({ totalReplyCount, replies }: ReplyListProps) => (
    <ListGroup variant={"flush"} className={"ps-4"}>
        {replies.map((reply) => (<ReplyListItem key={reply.id} reply={reply}/>))}
        {totalReplyCount > replies.length &&
            <ListGroupItem key={"more"} className={"align-items-center d-flex"}>
                <ReplyAll className={"me-2"}/>
                <span className={"small"}>
                {pluralize(totalReplyCount - replies.length, "more reply...", "more replies...")}
                </span>
            </ListGroupItem>
        }
    </ListGroup>
)
