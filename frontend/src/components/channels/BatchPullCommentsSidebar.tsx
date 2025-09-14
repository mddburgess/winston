import { Button, Col, Container, Offcanvas, Row } from "react-bootstrap";
import { BasicProgressBar } from "#/components/BasicProgressBar";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { PullCommentsList } from "#/components/pull/PullCommentsList";
import { useAppSelector } from "#/store/hooks";
import { pluralize } from "#/utils";

const BatchPullCommentsSidebar = (props: { show: boolean; onHide: () => void }) => {
  const { active, requested } = useAppSelector((state) => state.pullComments);

  return (
    <Offcanvas show={props.show} placement={"end"}>
      <Offcanvas.Header>
        <Offcanvas.Title>Pull comments</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body className={"bg-primary-subtle height-fit text-primary-emphasis"}>
        Pulling comments for <strong>{pluralize(requested.length, "video")}</strong>
        <BasicProgressBar completed={0} total={requested.length} />
      </Offcanvas.Body>
      <Offcanvas.Body className={"border-bottom border-top p-0"}>
        <Container className={"px-0"}>
          <PullCommentsList />
        </Container>
      </Offcanvas.Body>
      <Offcanvas.Body className={"bg-body-tertiary height-fit"}>
        <Row className={"flex-center"}>
          <Col>
            <AvailableQuota />
          </Col>
          <Col xs={"auto"}>
            <Button onClick={props.onHide} disabled={active}>
              Close
            </Button>
          </Col>
        </Row>
      </Offcanvas.Body>
    </Offcanvas>
  );
};

export { BatchPullCommentsSidebar };
