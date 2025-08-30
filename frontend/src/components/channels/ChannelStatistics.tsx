import { Col, Row } from "react-bootstrap";
import { BellFill, EyeFill, Youtube } from "react-bootstrap-icons";
import { formatNumber } from "#/utils/formatNumber";
import type { ChannelProps } from "#/types";

const ChannelStatistics = ({ channel }: ChannelProps) => (
  <Row className={"flex-center"}>
    <Col xs={"auto"}>
      <Row className={"g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          <Youtube />
        </Col>
        <Col xs={"auto"}>{Math.max(channel.video_count ?? 0, channel.statistics.video_count)}</Col>
      </Row>
    </Col>
    <Col xs={"auto"}>
      <Row className={"g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          <EyeFill />
        </Col>
        <Col xs={"auto"}>{formatNumber(channel.statistics.view_count)}</Col>
      </Row>
    </Col>
    <Col xs={"auto"}>
      <Row className={"g-2"}>
        <Col xs={"auto"} className={"flex-center"}>
          <BellFill />
        </Col>
        <Col xs={"auto"}>{formatNumber(channel.statistics.subscriber_count)}</Col>
      </Row>
    </Col>
  </Row>
);

export { ChannelStatistics };
