import { Card, Col, Image, Row } from "react-bootstrap";
import { ArrowDownRightCircle, ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { ChannelAuthorFocus } from "#/components/channels/ChannelAuthorFocus";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";
import { routes } from "#/utils/links";
import { ChannelStatistics } from "./ChannelStatistics";
import type { ChannelProps } from "#/types";

const ChannelCard = ({ channel }: ChannelProps) => {
  const opacity = channel.properties?.archived ? "opacity-25" : "opacity-100";

  return (
    <Card className={`h-100 ${opacity}`}>
      <Card.Body className={"p-2"}>
        <Row className={"m-0"}>
          <Col className={"col-2 col-md-3 p-0"}>
            <Image roundedCircle className={"border w-100"} src={channel.thumbnail_url} />
          </Col>
          <Col className={"col-10 col-md-9"}>
            <p className={"fs-5 mb-1 line-clamp-1"}>
              <Link to={routes.channels.details(channel.handle)}>{channel.title}</Link>
            </p>
            <p className={"mb-1 small line-clamp-2"}>{channel.description}</p>
          </Col>
        </Row>
      </Card.Body>
      <ChannelAuthorFocus channel={channel} />
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
  );
};

export { ChannelCard };
