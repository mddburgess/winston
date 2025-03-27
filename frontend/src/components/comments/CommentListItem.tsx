import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyList} from "./ReplyList";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";
import {Date} from "../Date";

type CommentListItemProps = {
    comment: CommentDto
}

export const CommentListItem = ({ comment }: CommentListItemProps) => (
    <ListGroupItem key={comment.id}>
        <Row>
            <Col xs={"auto"} className={"small"}>
                <Link to={`/authors/${comment.author.id}`}>
                    {comment.author.displayName}
                </Link>
            </Col>
            <Col xs={"auto"} className={"ps-0 small"}>
                <Date date={comment.publishedAt}/>
            </Col>
        </Row>
        <Row>
            <Col>
                {comment.text}
            </Col>
        </Row>
        <Row>
            <ReplyList replies={comment.replies}/>
        </Row>
    </ListGroupItem>
)
