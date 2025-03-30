import {useParams} from "react-router";
import {Row} from "react-bootstrap";
import {useFindAuthorDetailsByIdQuery} from "../../../store/slices/api";
import {VideoCommentsList} from "./VideoCommentsList";

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
            <VideoCommentsList {...authorDetails}/>
        </>
    );
}
