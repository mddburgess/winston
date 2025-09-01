import { Col, ListGroup, ListGroupItem, Row, Spinner } from "react-bootstrap";
import { CheckCircleFill, ExclamationDiamondFill, XOctagonFill } from "react-bootstrap-icons";
import { useAppSelector } from "#/store/hooks";
import type { Channel, Problem } from "#/api";
import type { ChannelListProps, PullOperationStatus } from "#/types";

const RefreshChannelsList = ({ channels }: ChannelListProps) => {
  const { responses, errors } = useAppSelector((state) => state.pullChannels);
  return (
    <ListGroup variant={"flush"}>
      {channels.map((channel, index) => (
        <RefreshChannelsListItem
          key={channel.id}
          index={index + 1}
          channel={channel}
          status={responses[channel.handle] ?? "ready"}
          error={errors[channel.handle]}
        />
      ))}
    </ListGroup>
  );
};

const indicators = {
  ready: <></>,
  fetching: <Spinner size={"sm"} variant={"info"} />,
  successful: <CheckCircleFill className={"text-success"} />,
  warning: <ExclamationDiamondFill className={"text-warning"} />,
  failed: <XOctagonFill className={"text-danger"} />,
};
const itemClassName = {
  ready: "",
  fetching: "bg-info-subtle border-info-subtle",
  successful: "bg-success-subtle border-success-subtle",
  warning: "bg-warning-subtle border-warning-subtle",
  failed: "bg-danger-subtle border-danger-subtle",
};

const RefreshChannelsListItem = (props: {
  index: number;
  channel: Channel;
  status: PullOperationStatus;
  error?: Problem;
}) => (
  <ListGroupItem className={itemClassName[props.status]}>
    <Row>
      <Col className={"line-clamp-1"}>
        <strong>{props.index}.</strong> {props.channel.title}
      </Col>
      <Col xs={"auto"} className={"flex-center"}>
        {indicators[props.status]}
      </Col>
    </Row>
    {props.error && (
      <Row>
        <Col className={"small"}>{props.error.detail}</Col>
      </Row>
    )}
  </ListGroupItem>
);

export { RefreshChannelsList };
