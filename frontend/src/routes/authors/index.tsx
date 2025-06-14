import { useMemo, useState } from "react";
import {
    Col,
    Image,
    ListGroup,
    ListGroupItem,
    Ratio,
    Row,
} from "react-bootstrap";
import { Link } from "react-router";
import { useListAuthorsQuery } from "#/api";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { routes } from "#/utils/links";
import { AuthorStatistics } from "./AuthorStatistics";

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
                        <ListGroup className={"mb-2"}>
                            {pageItems.map((author) => (
                                <ListGroupItem
                                    className={"py-0"}
                                    key={author.id}
                                >
                                    <Row>
                                        <Col
                                            xs={"auto"}
                                            className={
                                                "align-items-center d-flex pe-0"
                                            }
                                        >
                                            <Ratio
                                                aspectRatio={"1x1"}
                                                style={{ minWidth: "32px" }}
                                            >
                                                <Image
                                                    className={"border"}
                                                    roundedCircle
                                                    src={
                                                        author.profile_image_url
                                                    }
                                                />
                                            </Ratio>
                                        </Col>
                                        <Col className={"py-2"}>
                                            <Link
                                                to={routes.authors.details(
                                                    author.handle,
                                                )}
                                            >
                                                {author.handle}
                                            </Link>
                                        </Col>
                                        <AuthorStatistics author={author} />
                                    </Row>
                                </ListGroupItem>
                            ))}
                        </ListGroup>
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
