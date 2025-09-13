import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch } from "#/store/hooks";
import { pullCommentsRequested } from "#/store/slices/pullComments";
import type { VideoProps } from "#/types";

const PullCommentsButton = ({ video }: VideoProps) => {
  const dispatch = useAppDispatch();

  const handleClick = () => {
    dispatch(pullCommentsRequested(video.id));
  };

  return <IconButton icon={ArrowDownRightCircleFill} label={"Pull comments"} onClick={handleClick} />;
};

export { PullCommentsButton };
