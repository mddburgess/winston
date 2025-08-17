import { Col, ListGroup, ListGroupItem, Row } from "react-bootstrap";
import { AuthorLink } from "#/components/authors/AuthorLink";
import { AuthorProfileImage } from "#/components/authors/AuthorProfileImage";
import { AuthorStatistics } from "#/components/authors/AuthorStatistics";
import type { AuthorListProps, AuthorProps } from "#/types";

const AuthorList = ({ authors }: AuthorListProps) => (
    <ListGroup className={"mb-2"}>
        {authors.map((author) => (
            <AuthorListItem key={author.id} author={author} />
        ))}
    </ListGroup>
);

const AuthorListItem = ({ author }: AuthorProps) => (
    <ListGroupItem className={"py-0"} key={author.id}>
        <Row>
            <Col xs={"auto"} className={"align-items-center d-flex pe-0"}>
                <AuthorProfileImage author={author} minWidth={"2rem"} />
            </Col>
            <Col className={"py-2"}>
                <AuthorLink className={"small"} author={author} />
            </Col>
            <AuthorStatistics author={author} />
        </Row>
    </ListGroupItem>
);

export { AuthorList };
