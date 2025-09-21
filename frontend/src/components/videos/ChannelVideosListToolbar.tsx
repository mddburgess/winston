import { Col, Row } from "react-bootstrap";
import { PageSizeControl } from "#/components/PageSizeControl";
import { PaginationControl } from "#/components/PaginationControl";
import { PaginationLabel } from "#/components/PaginationLabel";
import { PullCommentsBatchButton } from "#/components/videos/PullCommentsBatchButton";
import { useAppDispatch } from "#/store/hooks";
import { setVideosPageSize } from "#/store/slices/preferences";
import type { VideoListProps } from "#/types";

type ChannelVideosListToolbarProps = VideoListProps & {
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const ChannelVideosListToolbar = ({ videos, totalCount, pageSize, pageNumber }: ChannelVideosListToolbarProps) => {
  const dispatch = useAppDispatch();

  const handleSetPageSize = (size: number) => {
    dispatch(setVideosPageSize(size));
  };

  return (
    <Row className={"mb-2"}>
      <Col className={"flex-center"}>
        <PaginationLabel
          itemLabel={"video"}
          itemCount={videos.length}
          totalCount={totalCount}
          pageSize={pageSize}
          pageNumber={pageNumber}
        />
      </Col>
      <PullCommentsBatchButton videos={videos} />
      <Col xs={"auto"}>
        <PageSizeControl pageSize={pageSize} setPageSize={handleSetPageSize} />
      </Col>
      <Col xs={"auto"}>
        <PaginationControl totalCount={totalCount} pageSize={pageSize} pageNumber={pageNumber} />
      </Col>
    </Row>
  );
};
export { ChannelVideosListToolbar };
