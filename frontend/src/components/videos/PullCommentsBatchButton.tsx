import { Col } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { pullCommentsRequested, setShowPullCommentsSidebar } from "#/store/slices/pullComments";
import { clearVideoSelection } from "#/store/slices/selections";
import type { VideoListProps } from "#/types";

const PullCommentsBatchButton = ({ videos }: VideoListProps) => {
  const dispatch = useAppDispatch();

  const selectedVideos = useAppSelector((state) => state.selections.videos);

  const handleClick = () => {
    const pullVideos = selectedVideos.length > 0 ? selectedVideos : videos;
    dispatch(pullCommentsRequested(pullVideos));
    dispatch(setShowPullCommentsSidebar(true));
    dispatch(clearVideoSelection());
  };

  return (
    <Col xs={"auto"}>
      <IconButton icon={ArrowDownRightCircleFill} label={"Pull comments"} onClick={handleClick} />
    </Col>
  );
};

export { PullCommentsBatchButton };
