import { Col, Row } from "react-bootstrap";
import { BellFill, EyeFill, PlayCircle, PlayCircleFill } from "react-bootstrap-icons";
import { IconLabel } from "#/components/IconLabel";
import { formatInteger } from "#/utils/formatInteger";
import type { ChannelProps } from "#/types";

type ChannelStatisticsProps = ChannelProps & {
  showPulledVideos?: boolean;
};

const ChannelStatistics = ({ channel, showPulledVideos = false }: ChannelStatisticsProps) => {
  const pulledVideos = channel.video_count ?? 0;
  const totalVideos = Math.max(pulledVideos, channel.statistics.video_count);

  const videoCountIcon = pulledVideos < totalVideos ? PlayCircle : PlayCircleFill;
  const videoCountLabel =
    showPulledVideos && pulledVideos < totalVideos
      ? `${pulledVideos} / ${channel.statistics.video_count}`
      : channel.statistics.video_count;

  return (
    <Row className={"flex-center"}>
      <Col xs={"auto"}>
        <IconLabel icon={videoCountIcon} label={videoCountLabel} />
      </Col>
      <Col xs={"auto"}>
        <IconLabel icon={EyeFill} label={formatInteger(channel.statistics.view_count)} />
      </Col>
      <Col xs={"auto"}>
        <IconLabel icon={BellFill} label={formatInteger(channel.statistics.subscriber_count)} />
      </Col>
    </Row>
  );
};

export { ChannelStatistics };
