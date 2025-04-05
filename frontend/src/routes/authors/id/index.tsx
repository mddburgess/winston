import {Link, useParams} from "react-router";
import {Breadcrumb, BreadcrumbItem, Row} from "react-bootstrap";
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
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{to: "/authors"}}>
                    Authors
                </BreadcrumbItem>
                <BreadcrumbItem active>
                    {authorDetails.author.displayName}
                </BreadcrumbItem>
            </Breadcrumb>
            <Row>
                <h1>{authorDetails.author.displayName}</h1>
            </Row>
            <VideoCommentsList {...authorDetails}/>
        </>
    );
}
