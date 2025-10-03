import { OverlayTrigger, Tooltip } from "react-bootstrap";
import { ChatDotsFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { pullCommentsRequested, setShowPullCommentsSidebar } from "#/store/slices/pullComments";
import { clearVideoSelection } from "#/store/slices/selections";
import { pluralize } from "#/utils";
import type { VideoListProps } from "#/types";

const PullCommentsBatchButton = ({ videos }: VideoListProps) => {
  const dispatch = useAppDispatch();

  const selectedVideos = useAppSelector((state) => state.selections.videos);
  const pullVideos = selectedVideos.length > 0 ? selectedVideos : videos;
  const tooltip = <Tooltip>Pull comments for {pluralize(pullVideos.length, "video")}</Tooltip>;

  const handleClick = () => {
    dispatch(pullCommentsRequested(pullVideos));
    dispatch(setShowPullCommentsSidebar(true));
    dispatch(clearVideoSelection());
  };

  return (
    <OverlayTrigger overlay={tooltip}>
      <IconButton icon={ChatDotsFill} onClick={handleClick} />
    </OverlayTrigger>
  );
};

export { PullCommentsBatchButton };
