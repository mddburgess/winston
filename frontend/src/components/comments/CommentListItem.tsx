import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyList} from "./ReplyList";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";

type CommentListItemProps = {
    comment: CommentDto
}

export const CommentListItem = ({ comment }: CommentListItemProps) => (
    <ListGroupItem key={comment.id}>
        <Row>
            <Col>
                <Link to={`/authors/${comment.author.id}`}>
                    {comment.author.displayName}
                </Link>
            </Col>
            <Col>
                {comment.text}
            </Col>
            <Col>
                {comment.publishedAt}
            </Col>
        </Row>
        <Row>
            <ReplyList replies={comment.replies}/>
        </Row>
    </ListGroupItem>
)
