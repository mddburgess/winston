import { ButtonGroup, ButtonToolbar, Col, Row } from "react-bootstrap";
import { PageSizeControl } from "#/components/PageSizeControl";
import { PaginationControl } from "#/components/PaginationControl";
import { PaginationLabel } from "#/components/PaginationLabel";
import { PullCommentsBatchButton } from "#/components/videos/PullCommentsBatchButton";
import { SelectVideosButton } from "#/components/videos/SelectVideosButton";
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
      <Col xs={"auto"}>
        <ButtonToolbar>
          <ButtonGroup className={"me-3"}>
            <SelectVideosButton />
            <PullCommentsBatchButton videos={videos} />
          </ButtonGroup>
          <ButtonGroup className={"me-3"}>
            <PaginationControl totalCount={totalCount} pageSize={pageSize} pageNumber={pageNumber} />
          </ButtonGroup>
          <ButtonGroup>
            <PageSizeControl pageSize={pageSize} setPageSize={handleSetPageSize} />
          </ButtonGroup>
        </ButtonToolbar>
      </Col>
    </Row>
  );
};
export { ChannelVideosListToolbar };
