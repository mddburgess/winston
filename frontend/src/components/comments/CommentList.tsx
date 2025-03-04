import {useListCommentsByVideoIdQuery} from "../../store/slices/api";
import {Col, ListGroup, Row} from "react-bootstrap";
import {ReplyFill} from "react-bootstrap-icons";
import {Link} from "react-router";

type CommentListProps = {
    videoId?: string;
}

export const CommentList = ({ videoId } : CommentListProps) => {

    if (videoId === undefined) {
        return <></>;
    }

    const {data: comments = []} = useListCommentsByVideoIdQuery(videoId)
    const renderedComments = comments.map(comment => (
        <ListGroup.Item key={comment.id}>
            <Row>
                <Col>
                    <Link to={`/authors/${comment.author?.id}`}>
                        {comment.author?.displayName}
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
                <ListGroup>
                    {comment.replies?.map(reply => (
                        <ListGroup.Item key={reply.id}>
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
                                <Col>{reply.publishedAt}</Col>
                            </Row>
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            </Row>
        </ListGroup.Item>
    ))

    return (
        <ListGroup>
            {renderedComments}
        </ListGroup>
    )
}
