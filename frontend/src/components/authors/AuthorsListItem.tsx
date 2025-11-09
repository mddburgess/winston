import { Col, ListGroupItem, Row } from "react-bootstrap";
import { AuthorLink } from "#/components/authors/AuthorLink";
import { AuthorProfileImage } from "#/components/authors/AuthorProfileImage";
import { AuthorStatistics } from "#/components/authors/AuthorStatistics";
import type { AuthorProps } from "#/types";

type AuthorsListItemProps = AuthorProps & {
  disabled: boolean;
};

const AuthorsListItem = ({ author, disabled }: AuthorsListItemProps) => (
  <ListGroupItem className={"py-0"} key={author.id} disabled={disabled}>
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

export { AuthorsListItem };
