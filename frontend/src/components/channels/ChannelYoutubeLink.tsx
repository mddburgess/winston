import { Col, Row } from "react-bootstrap";
import { CopyToClipboard } from "#/components/CopyToClipboard";
import type { ChannelProps } from "#/types";

const ChannelYoutubeLink = ({ channel }: ChannelProps) => {
  const youtubeUrl = `https://www.youtube.com/${channel.handle}`;

  return (
    <Row className={"g-0"}>
      <Col xs={"auto"}>
        <a href={youtubeUrl} target={"_blank"} rel={"noreferrer"}>
          {channel.handle}
        </a>
      </Col>
      <Col xs={"auto"} className={"flex-center"}>
        <CopyToClipboard text={youtubeUrl} />
      </Col>
    </Row>
  );
};

export { ChannelYoutubeLink };
