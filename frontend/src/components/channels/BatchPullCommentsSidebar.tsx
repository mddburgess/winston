import { filter, map } from "lodash";
import { Button, Col, Container, Offcanvas, Row } from "react-bootstrap";
import { BasicProgressBar } from "#/components/BasicProgressBar";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { PullCommentsList } from "#/components/pull/PullCommentsList";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { getPullCommentsStatus, setShowPullCommentsSidebar } from "#/store/slices/pullComments";
import { pluralize } from "#/utils";

const BatchPullCommentsSidebar = () => {
  const dispatch = useAppDispatch();
  const { active, requested, responses, showPullCommentsSidebar } = useAppSelector((state) => state.pullComments);

  const currentVideoIds = map(requested, "id");
  const completedResponses = filter(responses, (response) => !!response).filter(
    (response) =>
      currentVideoIds.includes(response.videoId) &&
      ["successful", "warning", "failed"].includes(getPullCommentsStatus(response)),
  ).length;

  const handleHide = () => {
    dispatch(setShowPullCommentsSidebar(false));
  };

  return (
    <Offcanvas show={showPullCommentsSidebar} placement={"end"}>
      <Offcanvas.Header>
        <Offcanvas.Title>Pull comments</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body className={"bg-primary-subtle height-fit text-primary-emphasis"}>
        Pulling comments for <strong>{pluralize(requested.length, "video")}</strong>
        <BasicProgressBar completed={completedResponses} total={requested.length} />
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
            <Button onClick={handleHide} disabled={active}>
              Close
            </Button>
          </Col>
        </Row>
      </Offcanvas.Body>
    </Offcanvas>
  );
};

export { BatchPullCommentsSidebar };
