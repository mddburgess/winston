import { Col, ListGroupItem, Row, Spinner } from "react-bootstrap";
import {
  ArrowDownRightCircleFill,
  CheckCircleFill,
  ExclamationDiamondFill,
  InfoCircleFill,
  XOctagonFill,
} from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { pullRepliesRequested } from "#/store/slices/pullReplies";
import { pluralize } from "#/utils";
import type { Problem, Video } from "#/api";
import type { PullOperationStatus, VideoProps } from "#/types";
import type { ReactNode } from "react";

const PullVideoRepliesActionItem = ({ video }: VideoProps) => {
  const { responses, errors } = useAppSelector((state) => state.pullReplies);

  const replyCount = video.comments?.reply_count ?? 0;
  const totalReplyCount = video.comments?.total_reply_count ?? 0;
  const repliesToPull = totalReplyCount - replyCount;

  if (repliesToPull <= 0 && !responses[video.id]) {
    return undefined;
  }

  const status = responses[video.id]?.status ?? "ready";
  const count = responses[video.id]?.count ?? 0;
  const error = errors[video.id];

  return (
    <ListGroupItem variant={statuses[status].variant}>
      <Row className={"flex-center g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          {statuses[status].icon}
        </Col>
        <Col>{statuses[status].message(count, error)}</Col>
        <Col xs={"auto"}>{statuses[status].action(video, count)}</Col>
      </Row>
    </ListGroupItem>
  );
};

const PullVideoRepliesAction = ({ video }: VideoProps) => {
  const dispatch = useAppDispatch();

  const handleClick = () => {
    dispatch(pullRepliesRequested({ videoId: video.id }));
  };

  return (
    <IconButton
      icon={ArrowDownRightCircleFill}
      label={"Pull replies"}
      variant={"link"}
      className={"m-0 p-0"}
      onClick={handleClick}
    />
  );
};

const statuses: Record<
  PullOperationStatus,
  {
    icon: ReactNode;
    variant: string;
    message: (count: number, error?: Problem) => string;
    action: (video: Video, count: number) => ReactNode;
  }
> = {
  ready: {
    icon: <InfoCircleFill className={"text-primary"} />,
    variant: "primary",
    message: () => "More replies may be pulled for comments on this video.",
    action: (video) => <PullVideoRepliesAction video={video} />,
  },
  fetching: {
    icon: <Spinner size={"sm"} variant={"info"} />,
    variant: "info",
    message: () => "Pulling replies for comments on this video...",
    action: (_, count) => pluralize(count, "reply", "replies"),
  },
  successful: {
    icon: <CheckCircleFill className={"text-success"} />,
    variant: "success",
    message: (count) => `Pulled ${pluralize(count, "reply", "replies")} for comments on this video.`,
    action: () => undefined,
  },
  warning: {
    icon: <ExclamationDiamondFill className={"text-warning"} />,
    variant: "warning",
    message: (_, error) => error?.detail ?? error?.title ?? "An unknown error has occurred.",
    action: () => undefined,
  },
  failed: {
    icon: <XOctagonFill className={"text-danger"} />,
    variant: "danger",
    message: (_, error) => error?.detail ?? error?.title ?? "An unknown error has occurred.",
    action: () => undefined,
  },
};

export { PullVideoRepliesActionItem };
