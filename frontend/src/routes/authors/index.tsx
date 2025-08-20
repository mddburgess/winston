import { useMemo, useState } from "react";
import { Col, Row } from "react-bootstrap";
import { useListAuthorsQuery } from "#/api";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { AuthorList } from "#/routes/authors/AuthorList";

export const AuthorListRoute = () => {
    const { data } = useListAuthorsQuery();
    const [search, setSearch] = useState("");
    const filteredAuthors = useMemo(
        () =>
            data?.authors.filter((author) =>
                author.handle.toLowerCase().includes(search.toLowerCase()),
            ) ?? [],
        [data, search],
    );

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>Authors</p>
                </Col>
            </Row>
            <PaginationContext pageSize={100} items={filteredAuthors}>
                {({
                    pageNumber,
                    setPageNumber,
                    pageSize,
                    pageCount,
                    pageItems,
                    totalItemCount,
                }) => (
                    <>
                        <PaginationRow
                            name={"author"}
                            total={totalItemCount}
                            pageSize={pageSize}
                            page={pageNumber}
                            setPage={setPageNumber}
                            search={search}
                            setSearch={setSearch}
                        />
                        <AuthorList authors={pageItems} />
                        {pageCount > 1 && (
                            <PaginationRow
                                name={"author"}
                                total={totalItemCount}
                                pageSize={pageSize}
                                page={pageNumber}
                                setPage={setPageNumber}
                            />
                        )}
                    </>
                )}
            </PaginationContext>
        </>
    );
};
