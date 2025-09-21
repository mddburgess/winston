import { Col, Row } from "react-bootstrap";
import { VideoCard } from "./VideoCard";
import type { VideoListProps } from "#/types";

type VideoCardsProps = VideoListProps & {
  disabled?: boolean;
};

const VideoCards = ({ videos, disabled }: VideoCardsProps) => (
  <Row xs={1} sm={2} md={3} lg={4} xl={5} xxl={6} className={"g-2"}>
    {videos.map((video) => (
      <Col key={video.id}>
        <VideoCard video={video} disabled={disabled} />
      </Col>
    ))}
  </Row>
);

export { VideoCards };
