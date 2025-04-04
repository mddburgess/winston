import {useListAuthorsQuery} from "../../store/slices/api";
import {Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import {PaginationRow} from "../../components/PaginationRow";
import {useMemo, useState} from "react";
import {Link} from "react-router";

export const AuthorsRoute = () => {

    const { data } = useListAuthorsQuery()

    const pageSize = 100;
    const [page, setPage] = useState(1);
    const [search, setSearch] = useState("");

    const displayedAuthors = useMemo(
        () => data?.authors
            .filter(author => author.displayName.toLowerCase().includes(search.toLowerCase()))
            .slice(pageSize * (page - 1), pageSize * page) ?? [],
        [data, pageSize, page, search]
    )

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>
                        Authors
                    </p>
                </Col>
            </Row>
            <PaginationRow
                name={"author"}
                total={data?.results ?? 0}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
                search={search}
                setSearch={setSearch}
            />
            <ListGroup className={"mb-2"}>
                {displayedAuthors.map((author) => (
                    <ListGroupItem key={author.id}>
                        <Link to={`/authors/${author.id}`}>
                            {author.displayName}
                        </Link>
                    </ListGroupItem>
                ))}
            </ListGroup>
            <PaginationRow
                name={"author"}
                total={data?.results ?? 0}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
            />
        </>
    )
}
