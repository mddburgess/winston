import { Pagination } from "react-bootstrap";
import { useSearchParams } from "react-router";

type PaginationControlProps = {
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const PaginationControl = (props: PaginationControlProps) => {
  const [_, setSearchParams] = useSearchParams();

  if (props.totalCount <= props.pageSize) {
    return undefined;
  }

  const oneIndexedPage = props.pageNumber + 1;
  const lastPage = Math.ceil(props.totalCount / props.pageSize);

  const handleClickFirst = () => {
    setSearchParams((searchParams) => {
      searchParams.set("p", "1");
      return searchParams;
    });
  };

  const handleClickPrev = () => {
    setSearchParams((searchParams) => {
      searchParams.set("p", `${oneIndexedPage - 1}`);
      return searchParams;
    });
  };

  const handleClickNext = () => {
    setSearchParams((searchParams) => {
      searchParams.set("p", `${oneIndexedPage + 1}`);
      return searchParams;
    });
  };

  const handleClickLast = () => {
    setSearchParams((searchParams) => {
      searchParams.set("p", `${lastPage}`);
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
