import { Breadcrumb, BreadcrumbItem, Row } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { useFindAuthorDetailsByHandleQuery } from "../../../store/slices/authors";
import { routes } from "../../../utils/links";
import { VideoCommentsList } from "./VideoCommentsList";

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
