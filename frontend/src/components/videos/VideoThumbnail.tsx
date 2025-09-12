import { Image, Ratio } from "react-bootstrap";
import type { VideoProps } from "#/types";

const VideoThumbnail = ({ video }: VideoProps) => (
  <Ratio aspectRatio={"4x3"}>
    <Image rounded src={video.thumbnail_url} />
  </Ratio>
);

export { VideoThumbnail };
