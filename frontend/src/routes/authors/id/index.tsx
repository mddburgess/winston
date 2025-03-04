import {useParams} from "react-router";
import {Container, Row} from "react-bootstrap";
import {useListCommentsByAuthorIdQuery} from "../../../store/slices/api";
import {CommentList} from "./CommentList";

export const AuthorsIdRoute = () => {
    const {authorId} = useParams()
    const {data: comments} = useListCommentsByAuthorIdQuery(authorId!)

    return (
        <Container>
            <Row>
                <h1>{authorId!}</h1>
            </Row>
            <CommentList comments={comments} />
        </Container>
    )
}
