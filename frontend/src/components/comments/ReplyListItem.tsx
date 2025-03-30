import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyFill} from "react-bootstrap-icons";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";
import {Date} from "../Date";

type ReplyListItemProps = {
    reply: CommentDto,
    highlightAuthorId?: string,
}

export const ReplyListItem = ({reply, highlightAuthorId = ""}: ReplyListItemProps) => {
    const highlight = highlightAuthorId === reply.author.id;

    return (
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
                <Col className={highlight ? "bg-info-subtle py-1 rounded text-info-emphasis" : ""}>
                    {reply.text}
                </Col>
            </Row>
        </ListGroupItem>
    );
}
