import { Card, Ratio, Row } from "react-bootstrap";
import { useNavigate } from "react-router";
import { routes } from "#/utils/links";
import { VideoCardSelectionCheckbox } from "./VideoCardSelectionCheckbox";
import { VideoCommentCounts } from "./VideoCommentCounts";
import { VideoPublishedAt } from "./VideoPublishedAt";
import type { VideoProps } from "#/types";

type VideoCardProps = VideoProps & {
  disabled?: boolean;
};

const VideoCard = ({ video, disabled = false }: VideoCardProps) => {
  const navigate = useNavigate();

  const cardClass = disabled ? "opacity-50" : "cursor-pointer hover-bg-info-subtle";

  const handleClick = () => {
    if (!disabled) {
      void navigate(routes.videos.details(video.id));
    }
  };

  return (
    <Card className={`h-100 ${cardClass}`} onClick={handleClick}>
      <VideoCardSelectionCheckbox video={video} />
      <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
        <Card.Img variant={"top"} src={video.thumbnail_url} />
      </Ratio>
      <Card.Body>
        <Card.Subtitle>{video.title}</Card.Subtitle>
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
};

export { VideoCard };
