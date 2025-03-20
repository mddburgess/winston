import {useParams} from "react-router";
import {Container, Row} from "react-bootstrap";
import {useListCommentsByAuthorIdQuery} from "../../../store/slices/api";
import {CommentList} from "../../../components/comments/CommentList";

export const AuthorsIdRoute = () => {
    const {authorId} = useParams()
    const {data: comments} = useListCommentsByAuthorIdQuery(authorId!)

    return (
        <>
            <Row>
                <h1>{authorId!}</h1>
            </Row>
            <CommentList comments={comments} />
        </>
    )
}
