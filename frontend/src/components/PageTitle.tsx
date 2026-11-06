import { Col, Row } from "react-bootstrap";

type PageTitleProps = {
  title: string;
};

const PageTitle = ({ title }: PageTitleProps) => (
  <Row>
    <Col>
      <h1>{title}</h1>
    </Col>
  </Row>
);

export { PageTitle };
