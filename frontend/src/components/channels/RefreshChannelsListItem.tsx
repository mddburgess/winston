import { Col, ListGroupItem, Row, Spinner } from "react-bootstrap";
import { CheckCircleFill, ExclamationDiamondFill, XOctagonFill } from "react-bootstrap-icons";
import type { Channel, Problem } from "#/api";
import type { PullOperationStatus } from "#/types";

const RefreshChannelsListItem = (props: {
  index: number;
  channel: Channel;
  status: PullOperationStatus;
  error?: Problem;
}) => (
  <ListGroupItem className={statuses[props.status].className}>
    <Row>
      <Col className={"line-clamp-1"}>
        <strong>{props.index}.</strong> {props.channel.title}
      </Col>
      <Col xs={"auto"} className={"flex-center"}>
        {statuses[props.status].icon}
      </Col>
    </Row>
    {props.error && (
      <Row>
        <Col className={"small"}>{props.error.detail}</Col>
      </Row>
    )}
  </ListGroupItem>
);

const statuses = {
  ready: {
    icon: <></>,
    className: "",
  },
  fetching: {
    icon: <Spinner size={"sm"} variant={"info"} />,
    className: "bg-info-subtle border-info-subtle",
  },
  successful: {
    icon: <CheckCircleFill className={"text-success"} />,
    className: "bg-success-subtle border-success-subtle",
  },
  warning: {
    icon: <ExclamationDiamondFill className={"text-warning"} />,
    className: "bg-warning-subtle border-warning-subtle",
  },
  failed: {
    icon: <XOctagonFill className={"text-danger"} />,
    className: "bg-danger-subtle border-danger-subtle",
  },
};

export { RefreshChannelsListItem };
