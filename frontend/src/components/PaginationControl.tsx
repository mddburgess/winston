import { Pagination } from "react-bootstrap";
import { useSearchParams } from "react-router";
import { parseIntOrDefault } from "#/utils";

type PaginationControlProps = {
  totalCount: number;
  pageSize: number;
};

const PaginationControl = (props: PaginationControlProps) => {
  const [searchParams, setSearchParams] = useSearchParams();

  if (props.totalCount <= props.pageSize) {
    return undefined;
  }

  const pageIndex = parseIntOrDefault(searchParams.get("i"), 0);
  const pageNumber = Math.floor(pageIndex / props.pageSize);
  const lastPageNumber = Math.floor((props.totalCount - 1) / props.pageSize);

  const setPageNumber = (pageNumber: number) => {
    setSearchParams((searchParams) => {
      searchParams.set("i", `${pageNumber * props.pageSize}`);
      return searchParams;
    });
  };

  const handleClickFirst = () => {
    setPageNumber(0);
  };

  const handleClickPrev = () => {
    setPageNumber(pageNumber - 1);
  };

  const handleClickNext = () => {
    setPageNumber(pageNumber + 1);
  };

  const handleClickLast = () => {
    setPageNumber(lastPageNumber);
  };

  return (
    <Pagination className={"mb-0"}>
      <Pagination.First data-testid={"firstPageButton"} disabled={pageNumber <= 0} onClick={handleClickFirst} />
      <Pagination.Prev data-testid={"prevPageButton"} disabled={pageNumber <= 0} onClick={handleClickPrev} />
      <Pagination.Item data-testid={"currentPageLabel"} active>
        {pageNumber + 1}
      </Pagination.Item>
      <Pagination.Next
        data-testid={"nextPageButton"}
        disabled={pageNumber >= lastPageNumber}
        onClick={handleClickNext}
      />
      <Pagination.Last
        data-testid={"lastPageButton"}
        disabled={pageNumber >= lastPageNumber}
        onClick={handleClickLast}
      />
    </Pagination>
  );
};

export { PaginationControl };
