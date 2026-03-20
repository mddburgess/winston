import { find } from "lodash";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams, useSearchParams } from "react-router";
import { AuthorInfoJumbotron } from "#/components/authors/AuthorInfoJumbotron";
import { AuthorDetailsTabs } from "#/routes/authors/id/AuthorDetailsTabs";
import { routes } from "#/utils/links";
import { AuthorSummaryQuery } from "./id/AuthorSummaryQuery";
import type { GetAuthorResp } from "#/api";

const AuthorDetailsRoute = () => {
  const { authorHandle = "" } = useParams();

  return (
    <AuthorSummaryQuery authorHandle={authorHandle}>
      {{
        isLoading: () => <div>Loading...</div>,
        isSuccess: (summary) => <AuthorDetailsContent summary={summary} />,
      }}
    </AuthorSummaryQuery>
  );
};

type AuthorDetailsContentProps = {
  summary: GetAuthorResp;
};

const AuthorDetailsContent = ({ summary }: AuthorDetailsContentProps) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const videoId = searchParams.get("v");
  const video = find(summary.videos, (v) => v.id === videoId);

  return (
    <>
      <Breadcrumb>
        <BreadcrumbItem linkAs={Link} linkProps={{ to: routes.authors.list }}>
          Authors
        </BreadcrumbItem>
        <BreadcrumbItem
          active={video === undefined}
          onClick={() => {
            setSearchParams({});
          }}
        >
          {summary.author.handle}
        </BreadcrumbItem>
        {video && <BreadcrumbItem active>{video.title}</BreadcrumbItem>}
      </Breadcrumb>
      <AuthorInfoJumbotron author={summary.author} video={video} />
      <AuthorDetailsTabs summary={summary} />
    </>
  );
};

export { AuthorDetailsRoute };
