import { Col, Row } from "react-bootstrap";
import { AuthorProfileImage } from "#/components/authors/AuthorProfileImage";
import { AuthorStatistics } from "#/components/authors/AuthorStatistics";
import type { AuthorProps } from "#/types";

const AuthorInfoJumbotron = ({ author }: AuthorProps) => (
    <Row className={"bg-body-tertiary border mx-0 my-3 p-3 rounded-3"}>
        <Col xs={"auto"} className={"flex-center px-1"}>
            <AuthorProfileImage author={author} minWidth={48} />
        </Col>
        <Col className={"h1 mb-0"}>{author.handle}</Col>
        <AuthorStatistics author={author} />
    </Row>
);

export { AuthorInfoJumbotron };
