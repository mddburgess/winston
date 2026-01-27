import { Col, Row, Tab, Tabs } from "react-bootstrap";
import { AddAuthorAliasForm } from "#/routes/authors/id/AddAuthorAliasForm";
import { AuthorAliasesList } from "#/routes/authors/id/AuthorAliasesList";
import { AuthorDetailsView } from "#/routes/authors/id/AuthorDetailsView";
import type { GetAuthorResp } from "#/api";

const AuthorDetailsTabs = (props: { summary: GetAuthorResp }) => (
  <Tabs className={"mb-3"}>
    <Tab eventKey={"videos"} title={"Videos"}>
      <AuthorDetailsView {...props.summary} />
    </Tab>
    <Tab eventKey={"aliases"} title={"Aliases"}>
      <Row className="gy-3">
        <Col xs={"12"}>
          <AuthorAliasesList author={props.summary.author} />
        </Col>
        <Col>
          <AddAuthorAliasForm author={props.summary.author} />
        </Col>
      </Row>
    </Tab>
  </Tabs>
);

export { AuthorDetailsTabs };
