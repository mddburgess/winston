import {useListAuthorsQuery} from "../../store/slices/authors";
import {Col, Image, ListGroup, ListGroupItem, Ratio, Row} from "react-bootstrap";
import {PaginationRow} from "../../components/PaginationRow";
import {useMemo, useState} from "react";
import {Link} from "react-router";
import {AuthorStatistics} from "./AuthorStatistics";

export const AuthorsRoute = () => {

    const {data} = useListAuthorsQuery()

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
                    <ListGroupItem
                        className={"py-0"}
                        key={author.id}
                    >
                        <Row>
                            <Col xs={"auto"} className={"align-items-center d-flex pe-0"}>
                                <Ratio aspectRatio={"1x1"} style={{ minWidth: "32px" }}>
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
                            <AuthorStatistics author={author}/>
                        </Row>
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
