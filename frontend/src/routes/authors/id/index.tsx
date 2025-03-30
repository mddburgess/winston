import {useParams} from "react-router";
import {Row} from "react-bootstrap";
import {useFindAuthorDetailsByIdQuery} from "../../../store/slices/api";
import {CommentList} from "../../../components/comments/CommentList";

export const AuthorsIdRoute = () => {
    const {authorId} = useParams()
    const {
        isSuccess,
        data: authorDetails,
    } = useFindAuthorDetailsByIdQuery(authorId!)

    return (isSuccess &&
        <>
            <Row>
                <h1>{authorDetails.author.displayName}</h1>
            </Row>
            <CommentList comments={authorDetails.comments} />
        </>
    );
}
