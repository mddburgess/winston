import { Card, Ratio, Row } from "react-bootstrap";
import { Link } from "react-router";
import { routes } from "#/utils/links";
import { VideoCardSelectionCheckbox } from "./VideoCardSelectionCheckbox";
import { VideoCommentCounts } from "./VideoCommentCounts";
import { VideoPublishedAt } from "./VideoPublishedAt";
import type { VideoProps } from "#/types";

const VideoCard = ({ video }: VideoProps) => (
  <Card className={"h-100"}>
    <VideoCardSelectionCheckbox video={video} />
    <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
      <Link to={routes.videos.details(video.id)}>
        <Card.Img variant={"top"} src={video.thumbnail_url} />
      </Link>
    </Ratio>
    <Card.Body>
      <Card.Subtitle>
        <Link to={routes.videos.details(video.id)}>{video.title}</Link>
      </Card.Subtitle>
    </Card.Body>
    <Card.Footer>
      <Row>
        <VideoCommentCounts video={video} showTotalReplyCount={false} />
      </Row>
    </Card.Footer>
    <Card.Footer>
      <VideoPublishedAt video={video} />
    </Card.Footer>
  </Card>
);

export { VideoCard };
