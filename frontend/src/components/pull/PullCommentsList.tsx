import { Col, ListGroup, ListGroupItem, Row } from "react-bootstrap";
import { BasicProgressBar } from "#/components/BasicProgressBar";
import { VideoCommentCounts } from "#/components/videos/VideoCommentCounts";
import { useAppSelector } from "#/store/hooks";
import { getPullCommentsStatus } from "#/store/slices/pullComments";
import type { PullOperationStatus, VideoProps } from "#/types";
import type { Record } from "react-bootstrap-icons";

const PullCommentsList = () => {
  const { requested } = useAppSelector((state) => state.pullComments);

  return (
    <ListGroup variant={"flush"}>
      {requested.map((video, index) => (
        <PullCommentsItem key={video.id} video={video} index={index} />
      ))}
    </ListGroup>
  );
};

type PullCommentsItemProps = VideoProps & {
  index: number;
};

const PullCommentsItem = ({ video, index }: PullCommentsItemProps) => {
  const { responses } = useAppSelector((state) => state.pullComments);
  const response = responses[video.id];

  const overallStatus = getPullCommentsStatus(response);
  const pulledCommentCount = response?.commentCount ?? 0;
  const pulledReplyCount = response?.replyIds.length ?? 0;
  const pulledCount = pulledCommentCount + pulledReplyCount;
  const totalCount =
    overallStatus === "ready"
      ? (video.details?.comment_count ?? 1)
      : overallStatus === "fetching"
        ? Math.max(video.details?.comment_count ?? NaN, pulledCount + 1)
        : pulledCount;

  return (
    <ListGroupItem variant={statuses[overallStatus].variant}>
      <Row>
        <Col className={"line-clamp-1"}>
          <strong>{index + 1}.</strong> {video.title}
        </Col>
        <Col xs={"auto"}>
          <Row className={"g-3 small"}>
            <VideoCommentCounts video={video} showTotalReplyCount={false} />
          </Row>
        </Col>
      </Row>
      <Row>
        <Col>
          <BasicProgressBar completed={pulledCount} total={totalCount} />
        </Col>
      </Row>
    </ListGroupItem>
  );
};

const statuses: Record<PullOperationStatus, { variant: string }> = {
  ready: { variant: "" },
  fetching: { variant: "info" },
  successful: { variant: "success" },
  warning: { variant: "warning" },
  failed: { variant: "danger" },
};

export { PullCommentsList };
