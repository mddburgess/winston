import { Col, ListGroup, Row } from "react-bootstrap";
import { EyeFill, HeartFill } from "react-bootstrap-icons";
import { BadgeList } from "#/components/BadgeList";
import { CopyToClipboard } from "#/components/CopyToClipboard";
import { IconLabel } from "#/components/IconLabel";
import { PullVideoCommentsActionItem } from "#/components/videos/PullVideoCommentsActionItem";
import { PullVideoRepliesActionItem } from "#/components/videos/PullVideoRepliesActionItem";
import { VideoChannelTitle } from "#/components/videos/VideoChannelTitle";
import { VideoCommentCounts } from "#/components/videos/VideoCommentCounts";
import { VideoPublishedAt } from "#/components/videos/VideoPublishedAt";
import { VideoThumbnail } from "#/components/videos/VideoThumbnail";
import { formatInteger } from "#/utils/formatInteger";
import { getTopicFromUrl } from "#/utils/getTopicFromUrl";
import type { VideoProps } from "#/types";

const VideoDetailsJumbotron = ({ video }: VideoProps) => (
  <ListGroup className={"my-3"}>
    <ListGroup.Item className={"bg-body-tertiary p-0"}>
      <Row className={"mx-0"}>
        <Col xs={12} sm={3} className={"p-0"}>
          <VideoThumbnail video={video} />
        </Col>
        <Col xs={12} sm={9} className={"px-3 py-2"}>
          <Row>
            <Col className={"h3"}>{video.title}</Col>
            <Col xs={"auto"}>
              <CopyToClipboard text={`https://www.youtube.com/watch?v=${video.id}`} />
            </Col>
          </Row>
          <Row className={"pb-2"}>
            <Col xs={"auto"}>
              <VideoChannelTitle video={video} />
            </Col>
            <Col xs={"auto"}>
              <VideoPublishedAt video={video} />
            </Col>
            {video.details && (
              <>
                <Col xs={"auto"}>
                  <IconLabel icon={EyeFill} label={formatInteger(video.details.view_count ?? 0)} />
                </Col>
                <Col xs={"auto"}>
                  <IconLabel icon={HeartFill} label={formatInteger(video.details.like_count ?? 0)} />
                </Col>
              </>
            )}
            <VideoCommentCounts video={video} />
          </Row>
          <Row className={"pb-2"}>
            <Col className={"small"}>{video.description}</Col>
          </Row>
          <Row>
            {video.details?.topics && (
              <Col xs={12} className={"mb-3 mb-lg-0"}>
                <h6>Topics</h6>
                <BadgeList values={video.details.topics} transformer={getTopicFromUrl} />
              </Col>
            )}
            {video.details?.tags && (
              <Col>
                <h6>Tags</h6>
                <BadgeList values={video.details.tags} />
              </Col>
            )}
          </Row>
        </Col>
      </Row>
    </ListGroup.Item>
    <PullVideoCommentsActionItem video={video} />
    <PullVideoRepliesActionItem video={video} />
  </ListGroup>
);

export { VideoDetailsJumbotron };
