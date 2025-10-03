import { Card, Ratio, Row } from "react-bootstrap";
import { useNavigate } from "react-router";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { toggleSelectVideo } from "#/store/slices/selections";
import { routes } from "#/utils/links";
import { VideoCommentCounts } from "./VideoCommentCounts";
import { VideoPublishedAt } from "./VideoPublishedAt";
import { VideoSelectedIcon } from "./VideoSelectedIcon";
import type { VideoProps } from "#/types";

type VideoCardProps = VideoProps & {
  disabled?: boolean;
};

const VideoCard = ({ video, disabled = false }: VideoCardProps) => {
  const dispatch = useAppDispatch();
  const selected = useAppSelector((state) => state.selections.videos.includes(video));
  const selectionActive = useAppSelector((state) => state.selections.active);

  const navigate = useNavigate();

  const cardClass = disabled
    ? "opacity-50"
    : selected
      ? "bg-primary-subtle border-primary cursor-pointer hover-bg-info-subtle "
      : "cursor-pointer hover-bg-info-subtle";

  const handleClick = () => {
    if (!disabled) {
      if (selectionActive) {
        dispatch(toggleSelectVideo(video));
      } else {
        void navigate(routes.videos.details(video.id));
      }
    }
  };

  return (
    <Card className={`h-100 ${cardClass}`} onClick={handleClick}>
      <VideoSelectedIcon video={video} />
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
