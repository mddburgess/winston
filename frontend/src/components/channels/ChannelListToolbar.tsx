import { ButtonGroup, ButtonToolbar, Col, Row } from "react-bootstrap";
import { EyeSlash } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { pluralize } from "#/utils";
import { PullChannelActions } from "./PullChannelActions";
import type { ChannelListProps } from "#/types";

type ChannelListToolbarProps = ChannelListProps & {
  onPullChannel: () => void;
  onRefreshChannels: () => void;
};

const ChannelListToolbar = (props: ChannelListToolbarProps) => {
  return (
    <Row className={"mb-2"}>
      <Col className={"flex-center"}>{pluralize(props.channels.length, "channel")}</Col>
      <Col xs={"auto"}>
        <ButtonToolbar>
          <ButtonGroup className={"me-2"}>
            <IconButton icon={EyeSlash} variant={"outline-secondary"} />
          </ButtonGroup>
          <ButtonGroup>
            <PullChannelActions {...props} />
          </ButtonGroup>
        </ButtonToolbar>
      </Col>
    </Row>
  );
};

export { ChannelListToolbar };
