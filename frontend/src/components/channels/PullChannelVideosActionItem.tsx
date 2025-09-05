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
import { pullVideosRequested } from "#/store/slices/pullVideos";
import { pluralize } from "#/utils";
import type { Channel, Problem } from "#/api";
import type { ChannelProps, PullOperationStatus } from "#/types";
import type { ReactNode } from "react";

const PullChannelVideosActionItem = ({ channel }: ChannelProps) => {
  const { responses, errors } = useAppSelector((state) => state.pullVideos);

  const status = responses[channel.handle]?.status ?? "ready";
  const count = responses[channel.id]?.count ?? 0;
  const error = errors[channel.handle];

  return (
    <ListGroupItem variant={statuses[status].variant}>
      <Row className={"flex-center g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          {statuses[status].icon}
        </Col>
        <Col>{statuses[status].message(count, error)}</Col>
        <Col xs={"auto"}>{statuses[status].action(channel, count)}</Col>
      </Row>
    </ListGroupItem>
  );
};

const PullChannelVideosAction = ({ channel }: ChannelProps) => {
  const dispatch = useAppDispatch();

  const handleClick = () => {
    dispatch(pullVideosRequested({ channelHandle: channel.handle, range: "latest" }));
  };

  return (
    <IconButton
      icon={ArrowDownRightCircleFill}
      label={"Pull new videos"}
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
    action: (channel: Channel, count: number) => ReactNode;
  }
> = {
  ready: {
    icon: <InfoCircleFill className={"text-primary"} />,
    variant: "primary",
    message: () => "New videos may be available to be pulled for this channel.",
    action: (channel) => <PullChannelVideosAction channel={channel} />,
  },
  fetching: {
    icon: <Spinner size={"sm"} variant={"info"} />,
    variant: "info",
    message: () => "Pulling new videos for this channel...",
    action: (_, count) => pluralize(count, "video"),
  },
  successful: {
    icon: <CheckCircleFill className={"text-success"} />,
    variant: "success",
    message: (count) => `Pulled ${pluralize(count, "new video")} for this channel.`,
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

export { PullChannelVideosActionItem };
