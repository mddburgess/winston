import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { routes } from "../../../utils/links";
import { AuthorSummary } from "./AuthorSummary";
import { AuthorSummaryQuery } from "./AuthorSummaryQuery";

export const AuthorDetailsRoute = () => {
    const { authorHandle } = useParams();
    return (
        <AuthorSummaryQuery authorHandle={authorHandle!}>
            {{
                isLoading: () => <div>Loading...</div>,
                isSuccess: (summary) => (
                    <>
                        <Breadcrumb>
                            <BreadcrumbItem
                                linkAs={Link}
                                linkProps={{ to: routes.authors.list }}
                            >
                                Authors
                            </BreadcrumbItem>
                            <BreadcrumbItem active>
                                {summary.author.displayName}
                            </BreadcrumbItem>
                        </Breadcrumb>
                        <h1>{summary.author.displayName}</h1>
                        <AuthorSummary
                            {...summary}
                            authorHandle={authorHandle!}
                        />
                    </>
                ),
            }}
        </AuthorSummaryQuery>
    );
};
