import { useNavigate, useSearchParams } from "react-router";
import { useListAuthorsQuery } from "#/api";
import { parseIntOrDefault } from "#/utils";
import { routes } from "#/utils/links";
import { AuthorsList } from "./AuthorsList";
import { AuthorsListToolbar } from "./AuthorsListToolbar";

const AllAuthorsList = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const search = searchParams.get("s") ?? undefined;
  const index = parseIntOrDefault(searchParams.get("i"), 0);
  const pageSize = 50;
  const pageNumber = Math.floor(index / pageSize);

  const { isFetching, isSuccess, data } = useListAuthorsQuery({ search, page: pageNumber, size: pageSize });

  if (isSuccess && data.authors.length === 1) {
    const authorHandle = data.authors[0].handle;
    if (authorHandle === `@${search}`) {
      void navigate(routes.authors.details(authorHandle));
    }
  }

  return (
    isSuccess && (
      <>
        <AuthorsListToolbar
          authors={data.authors}
          totalCount={data.results.total_count}
          pageSize={pageSize}
          pageNumber={pageNumber}
        />
        <AuthorsList authors={data.authors} disabled={isFetching} />
      </>
    )
  );
};

export { AllAuthorsList };
