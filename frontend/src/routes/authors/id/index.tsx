import { Link, useParams } from "react-router";
import { Breadcrumb, BreadcrumbItem, Row } from "react-bootstrap";
import { useFindAuthorDetailsByHandleQuery } from "../../../store/slices/authors";
import { VideoCommentsList } from "./VideoCommentsList";
import { routes } from "../../../utils/links";

export const AuthorDetailsRoute = () => {
    const { authorHandle } = useParams();
    const { isSuccess, data: authorDetails } =
        useFindAuthorDetailsByHandleQuery(authorHandle!);

    return (
        isSuccess && (
            <>
                <Breadcrumb>
                    <BreadcrumbItem
                        linkAs={Link}
                        linkProps={{ to: routes.authors.list }}
                    >
                        Authors
                    </BreadcrumbItem>
                    <BreadcrumbItem active>
                        {authorDetails.author.displayName}
                    </BreadcrumbItem>
                </Breadcrumb>
                <Row>
                    <h1>{authorDetails.author.displayName}</h1>
                </Row>
                <VideoCommentsList {...authorDetails} />
            </>
        )
    );
};
