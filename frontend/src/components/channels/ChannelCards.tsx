import { Col, Row } from "react-bootstrap";
import { ChannelCard } from "./ChannelCard";
import type { ChannelListProps } from "#/types";

const ChannelCards = ({ channels }: ChannelListProps) => (
  <Row xs={1} md={2} lg={3} xxl={4} className={"g-2"}>
    {channels.map((channel) => (
      <Col key={channel.id}>
        <ChannelCard channel={channel} />
      </Col>
    ))}
  </Row>
);

export { ChannelCards };
