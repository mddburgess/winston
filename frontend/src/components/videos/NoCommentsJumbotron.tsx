import { Col, Row } from "react-bootstrap";
import { PullCommentsButton } from "./PullCommentsButton";
import type { VideoProps } from "#/types";

const NoCommentsJumbotron = ({ video }: VideoProps) => {
  const hasBeenFetched = video.comments?.last_fetched_at !== undefined;
  return (
    <Row className={"border border-dashed mx-0 my-3 p-5 rounded-3"}>
      <Col className={"text-center"}>
        <Row>
          <Col>
            <h2>{hasBeenFetched ? `This video has no comments` : `Comments haven't been pulled yet`}</h2>
          </Col>
        </Row>
        {!hasBeenFetched && (
          <Row className={"mt-3"}>
            <Col>
              <PullCommentsButton video={video} />
            </Col>
          </Row>
        )}
      </Col>
    </Row>
  );
};

export { NoCommentsJumbotron };
