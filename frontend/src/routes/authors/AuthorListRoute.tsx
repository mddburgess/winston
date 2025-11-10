import { Col, Row } from "react-bootstrap";
import { AllAuthorsList } from "#/components/authors/AllAuthorsList";

export const AuthorListRoute = () => {
  return (
    <>
      <Row className={"mb-2"}>
        <Col className={"align-items-center d-flex"}>
          <p className={"h1 m-0"}>Authors</p>
        </Col>
      </Row>
      <AllAuthorsList />
    </>
  );
};
