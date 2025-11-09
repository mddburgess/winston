import { ButtonGroup, ButtonToolbar, Col, Row } from "react-bootstrap";
import { PaginationControl } from "#/components/PaginationControl";
import { PaginationLabel } from "#/components/PaginationLabel";
import type { AuthorListProps } from "#/types";

type AuthorsListToolbarProps = AuthorListProps & {
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const AuthorsListToolbar = (props: AuthorsListToolbarProps) => {
  return (
    <Row className={"mb-2"}>
      <Col className={"flex-center"}>
        <PaginationLabel
          itemLabel={"author"}
          itemCount={props.authors.length}
          totalCount={props.totalCount}
          pageSize={props.pageSize}
          pageNumber={props.pageNumber}
        />
      </Col>
      <Col xs={"auto"}>
        <ButtonToolbar>
          <ButtonGroup>
            <PaginationControl totalCount={props.totalCount} pageSize={props.pageSize} />
          </ButtonGroup>
        </ButtonToolbar>
      </Col>
    </Row>
  );
};

export { AuthorsListToolbar };
