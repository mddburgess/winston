import {useListCommentsByVideoIdQuery} from "../../store/slices/api";
import {Col, ListGroup, Row} from "react-bootstrap";

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
                    {comment.author?.displayName}
                </Col>
                <Col>
                    {comment.text}
                </Col>
                <Col>
                    {comment.publishedAt}
                </Col>
            </Row>
        </ListGroup.Item>
    ))

    return (
        <ListGroup>
            {renderedComments}
        </ListGroup>
    )
}
