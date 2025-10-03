import { HandIndexThumbFill } from "react-bootstrap-icons";
import { useAppSelector } from "#/store/hooks";
import type { VideoProps } from "#/types";

const VideoSelectedIcon = ({ video }: VideoProps) => {
  const selected = useAppSelector((state) => state.selections.videos.includes(video));

  return selected && <HandIndexThumbFill className={"text-info video-selected"} />;
};

export { VideoSelectedIcon };
