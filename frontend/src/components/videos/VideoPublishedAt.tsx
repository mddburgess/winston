import { ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";
import type { VideoProps } from "#/types";

const VideoPublishedAt = ({ video }: VideoProps) => (
  <IconLabel icon={ArrowUpLeftCircleFill}>
    <Date date={video.published_at} />
  </IconLabel>
);

export { VideoPublishedAt };
