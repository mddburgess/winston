import { Col, ListGroupItem, Row, Spinner } from "react-bootstrap";
import { CheckCircleFill, ExclamationDiamondFill, XOctagonFill } from "react-bootstrap-icons";
import { BasicProgressBar } from "#/components/BasicProgressBar";
import { useAppSelector } from "#/store/hooks";
import { getPullCommentsStatus } from "#/store/slices/pullComments";
import { pluralize } from "#/utils";
import type { Problem } from "#/api";
import type { PullOperationStatus, VideoProps } from "#/types";
import type { ReactNode } from "react";

const PullVideoCommentsActionItem = ({ video }: VideoProps) => {
  const { active, responses, errors } = useAppSelector((state) => state.pullComments);
  const response = responses[video.id];

  if (!active && !response) {
    return undefined;
  }

  const overallStatus = getPullCommentsStatus(response);
  const pulledCommentCount = response?.commentCount ?? 0;
  const pulledReplyCount = response?.replyIds.length ?? 0;
  const pulledCount = pulledCommentCount + pulledReplyCount;
  const totalCount = Math.max(video.details?.comment_count ?? NaN, pulledCount + 1);
  const error = errors[video.id];

  return (
    <ListGroupItem variant={statuses[overallStatus].variant}>
      <Row className={"flex-center g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          {statuses[overallStatus].icon}
        </Col>
        <Col xs={"auto"}>{statuses[overallStatus].message(pulledCount, error)}</Col>
        <Col>
          {statuses[overallStatus].showProgressBar && <BasicProgressBar completed={pulledCount} total={totalCount} />}
        </Col>
      </Row>
    </ListGroupItem>
  );
};

const statuses: Record<
  PullOperationStatus,
  { icon: ReactNode; variant: string; message: (count: number, error?: Problem) => string; showProgressBar: boolean }
> = {
  ready: {
    icon: <Spinner size={"sm"} variant={"info"} />,
    variant: "info",
    message: () => "Pulling comments...",
    showProgressBar: true,
  },
  fetching: {
    icon: <Spinner size={"sm"} variant={"info"} />,
    variant: "info",
    message: () => "Pulling comments...",
    showProgressBar: true,
  },
  successful: {
    icon: <CheckCircleFill className={"text-success"} />,
    variant: "success",
    message: (count) => `Pulled ${pluralize(count, "comment")} for this video.`,
    showProgressBar: false,
  },
  warning: {
    icon: <ExclamationDiamondFill className={"text-warning"} />,
    variant: "warning",
    message: (_, error) => error?.detail ?? error?.title ?? "An unknown error has occurred.",
    showProgressBar: false,
  },
  failed: {
    icon: <XOctagonFill className={"text-danger"} />,
    variant: "danger",
    message: (_, error) => error?.detail ?? error?.title ?? "An unknown error has occurred.",
    showProgressBar: false,
  },
};

export { PullVideoCommentsActionItem };
