import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyFill} from "react-bootstrap-icons";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";

type ReplyListItemProps = {
    reply: CommentDto
}

export const ReplyListItem = ({reply}: ReplyListItemProps) => (
    <ListGroupItem key={reply.id}>
        <Row>
            <Col>
                <ReplyFill/>
                <Link to={`/authors/${reply.author?.id}`}>
                    {reply.author?.displayName}
                </Link>
            </Col>
            <Col>
                {reply.text}
            </Col>
            <Col>
                {reply.publishedAt}
            </Col>
        </Row>
    </ListGroupItem>
)
