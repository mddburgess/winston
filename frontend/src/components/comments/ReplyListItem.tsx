import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyFill} from "react-bootstrap-icons";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";
import {Date} from "../Date";

type ReplyListItemProps = {
    reply: CommentDto
}

export const ReplyListItem = ({reply}: ReplyListItemProps) => (
    <ListGroupItem key={reply.id}>
        <Row>
            <Col xs={"auto"} className={"align-items-center d-flex"}>
                <ReplyFill className={"me-2"}/>
                <Link to={`/authors/${reply.author?.id}`} className={"small"}>
                    {reply.author?.displayName}
                </Link>
            </Col>
            <Col xs={"auto"} className={"ps-0 small"}>
                <Date date={reply.publishedAt}/>
            </Col>
        </Row>
        <Row>
            <Col>
                {reply.text}
            </Col>
        </Row>
    </ListGroupItem>
)
