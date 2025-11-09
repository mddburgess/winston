import { useSearchParams } from "react-router";
import { useListAuthorsQuery } from "#/api";
import { parseIntOrDefault } from "#/utils";
import { AuthorsList } from "./AuthorsList";
import { AuthorsListToolbar } from "./AuthorsListToolbar";

const AllAuthorsList = () => {
  const [searchParams] = useSearchParams();

  const index = parseIntOrDefault(searchParams.get("i"), 0);
  const pageSize = 50;
  const pageNumber = Math.floor(index / pageSize);

  const { isFetching, isSuccess, data } = useListAuthorsQuery({ page: pageNumber, size: pageSize });

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
