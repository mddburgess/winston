import { Col, Row } from "react-bootstrap";
import { PaginationControl } from "#/components/PaginationControl";
import { PaginationLabel } from "#/components/PaginationLabel";
import type { VideoListProps } from "#/types";

type ChannelVideosListToolbarProps = VideoListProps & {
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const ChannelVideosListToolbar = ({ videos, totalCount, pageSize, pageNumber }: ChannelVideosListToolbarProps) => {
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
        <PaginationControl totalCount={totalCount} pageSize={pageSize} pageNumber={pageNumber} />
      </Col>
    </Row>
  );
};
export { ChannelVideosListToolbar };
