import { useListAuthorsQuery } from "../../store/slices/authors";
import {
    Col,
    Image,
    ListGroup,
    ListGroupItem,
    Ratio,
    Row,
} from "react-bootstrap";
import { PaginationRow } from "../../components/PaginationRow";
import { useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router";
import { AuthorStatistics } from "./AuthorStatistics";

export const AuthorsRoute = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const { data } = useListAuthorsQuery();

    const pageSize = 100;
    const [search, setSearch] = useState("");

    const displayedAuthors = useMemo(() => {
        const page = parseInt(searchParams.get("p") ?? "1");
        return (
            data?.authors
                .filter((author) =>
                    author.displayName
                        .toLowerCase()
                        .includes(search.toLowerCase()),
                )
                .slice(pageSize * (page - 1), pageSize * page) ?? []
        );
    }, [data, pageSize, searchParams, search]);

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>Authors</p>
                </Col>
            </Row>
            <PaginationRow
                name={"author"}
                total={data?.results ?? 0}
                pageSize={pageSize}
                page={parseInt(searchParams.get("p") ?? "1")}
                setPage={(page) => setSearchParams({ p: `${page}` })}
                search={search}
                setSearch={setSearch}
            />
            <ListGroup className={"mb-2"}>
                {displayedAuthors.map((author) => (
                    <ListGroupItem className={"py-0"} key={author.id}>
                        <Row>
                            <Col
                                xs={"auto"}
                                className={"align-items-center d-flex pe-0"}
                            >
                                <Ratio
                                    aspectRatio={"1x1"}
                                    style={{ minWidth: "32px" }}
                                >
                                    <Image
                                        className={"border"}
                                        roundedCircle
                                        src={author.profileImageUrl}
                                    />
                                </Ratio>
                            </Col>
                            <Col className={"py-2"}>
                                <Link to={`/authors/${author.id}`}>
                                    {author.displayName}
                                </Link>
                            </Col>
                            <AuthorStatistics author={author} />
                        </Row>
                    </ListGroupItem>
                ))}
            </ListGroup>
            <PaginationRow
                name={"author"}
                total={data?.results ?? 0}
                pageSize={pageSize}
                page={parseInt(searchParams.get("p") ?? "1")}
                setPage={(page) => setSearchParams({ p: `${page}` })}
            />
        </>
    );
};
