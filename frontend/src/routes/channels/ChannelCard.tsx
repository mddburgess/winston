import { Card, Col, Image, Row } from "react-bootstrap";
import { ArrowDownRightCircle, ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { ChannelStatistics } from "#/components/channels/ChannelStatistics";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";
import { routes } from "#/utils/links";
import type { ChannelProps } from "#/types";

export const ChannelCard = ({ channel }: ChannelProps) => (
  <Col className={"g-2"}>
    <Card className={"h-100"}>
      <Card.Body className={"p-2"}>
        <Row className={"m-0"}>
          <Col className={"col-2 col-md-3 p-0"}>
            <Image roundedCircle className={"border w-100"} src={channel.thumbnail_url} />
          </Col>
          <Col className={"col-10 col-md-9"}>
            <p className={"fs-5 mb-1"}>
              <Link to={routes.channels.details(channel.handle)}>{channel.title}</Link>
            </p>
            <p className={"mb-1 small line-clamp"}>{channel.description}</p>
          </Col>
        </Row>
      </Card.Body>
      <Card.Footer>
        <ChannelStatistics channel={channel} />
      </Card.Footer>
      <Card.Footer>
        <Row>
          <Col xs={"auto"}>
            <IconLabel icon={ArrowUpLeftCircleFill}>
              <Date date={channel.published_at} />
            </IconLabel>
          </Col>
          <Col xs={"auto"}>
            <IconLabel icon={ArrowDownRightCircle}>
              <Date date={channel.last_fetched_at} />
            </IconLabel>
          </Col>
        </Row>
      </Card.Footer>
    </Card>
  </Col>
);
