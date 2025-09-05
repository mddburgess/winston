import { Col, ListGroup, Row } from "react-bootstrap";
import { BadgeList } from "#/components/BadgeList";
import { ChannelStatistics } from "#/components/channels/ChannelStatistics";
import { ChannelThumbnail } from "#/components/channels/ChannelThumbnail";
import { ChannelYoutubeLink } from "#/components/channels/ChannelYoutubeLink";
import { PullChannelVideosActionItem } from "#/components/channels/PullChannelVideosActionItem";
import { RefreshChannelButton } from "#/components/channels/RefreshChannelButton";
import { getTopicFromUrl } from "#/utils/getTopicFromUrl";
import type { ChannelProps } from "#/types";

const ChannelDetailsJumbotron = ({ channel }: ChannelProps) => (
  <ListGroup className={"bg-body-tertiary my-3 rounded-3"}>
    <ListGroup.Item className={"bg-body-tertiary p-0"}>
      <Row className={"mx-0"}>
        <Col xs={"auto"} className={"py-2"}>
          <ChannelThumbnail channel={channel} />
        </Col>
        <Col className={"py-2"}>
          <Row>
            <Col>
              <h1>{channel.title}</h1>
            </Col>
            <Col xs={"auto"}>
              <RefreshChannelButton channel={channel} />
            </Col>
          </Row>
          <Row className={"pb-2"}>
            <Col xs={"auto"}>
              <ChannelYoutubeLink channel={channel} />
            </Col>
            <Col>
              <ChannelStatistics channel={channel} showPulledVideos={true} />
            </Col>
          </Row>

          <p className={"small"}>{channel.description}</p>
          <Row xs={1} lg={2}>
            <Col className={"mb-3 mb-lg-0"}>
              <p className={"h6"}>Topics</p>
              <BadgeList values={channel.topics} transformer={getTopicFromUrl} />
            </Col>
            <Col>
              <p className={"h6"}>Keywords</p>
              <BadgeList values={channel.keywords} />
            </Col>
          </Row>
        </Col>
      </Row>
    </ListGroup.Item>
    <PullChannelVideosActionItem channel={channel} />
  </ListGroup>
);

export { ChannelDetailsJumbotron };
