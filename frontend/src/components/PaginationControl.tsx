import { Pagination } from "react-bootstrap";
import { useSearchParams } from "react-router";
import { parseIntOrDefault } from "#/utils";

type PaginationControlProps = {
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const PaginationControl = (props: PaginationControlProps) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const index = parseIntOrDefault(searchParams.get("i"), 0);

  if (props.totalCount <= props.pageSize) {
    return undefined;
  }

  const pageIndex = Math.floor(index / props.pageSize) * props.pageSize;
  const prevPageIndex = pageIndex - props.pageSize;
  const nextPageIndex = pageIndex + props.pageSize;
  const lastPageIndex = Math.floor(props.totalCount / props.pageSize) * props.pageSize;

  const oneIndexedPage = props.pageNumber + 1;
  const lastPage = Math.ceil(props.totalCount / props.pageSize);

  const handleClickFirst = () => {
    setSearchParams((searchParams) => {
      searchParams.set("i", "0");
      return searchParams;
    });
  };

  const handleClickPrev = () => {
    setSearchParams((searchParams) => {
      searchParams.set("i", `${prevPageIndex}`);
      return searchParams;
    });
  };

  const handleClickNext = () => {
    setSearchParams((searchParams) => {
      searchParams.set("i", `${nextPageIndex}`);
      return searchParams;
    });
  };

  const handleClickLast = () => {
    setSearchParams((searchParams) => {
      searchParams.set("i", `${lastPageIndex}`);
      return searchParams;
    });
  };

  return (
    <Pagination className={"mb-0"}>
      <Pagination.First disabled={oneIndexedPage <= 1} onClick={handleClickFirst} />
      <Pagination.Prev disabled={oneIndexedPage <= 1} onClick={handleClickPrev} />
      <Pagination.Item active>{oneIndexedPage}</Pagination.Item>
      <Pagination.Next disabled={oneIndexedPage >= lastPage} onClick={handleClickNext} />
      <Pagination.Last disabled={oneIndexedPage >= lastPage} onClick={handleClickLast} />
    </Pagination>
  );
};

export { PaginationControl };
