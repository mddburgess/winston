import { Col, Row } from "react-bootstrap";
import type { CommentStatus } from "#/api";

type CommentsUnavailableJumbotronProps = {
  commentStatus: CommentStatus;
};

export const CommentsUnavailableJumbotron = ({ commentStatus }: CommentsUnavailableJumbotronProps) => (
  <Row className={"bg-secondary-subtle border border-secondary mx-0 my-3 p-5 rounded-3 text-secondary-emphasis"}>
    <Col>
      <h2 className={"text-center"}>Comments are {commentStatus} for this video</h2>
    </Col>
  </Row>
);
