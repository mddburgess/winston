import { isEmpty } from "lodash";
import { ButtonGroup, Dropdown } from "react-bootstrap";
import { ArrowDownRightCircleFill, ArrowRepeat } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { IconLabel } from "#/components/IconLabel";
import type { ChannelListProps } from "#/types";

type PullChannelActionsProps = ChannelListProps & {
  onPullChannel: () => void;
  onRefreshChannels: () => void;
};

const PullChannelActions = (props: PullChannelActionsProps) =>
  isEmpty(props.channels) ? (
    <PullChannelButton onPullChannel={props.onPullChannel} />
  ) : (
    <PullChannelDropdown {...props} />
  );

const PullChannelDropdown = (props: PullChannelActionsProps) => (
  <Dropdown as={ButtonGroup}>
    <PullChannelButton onPullChannel={props.onPullChannel} />
    <Dropdown.Toggle />
    <Dropdown.Menu align={"end"}>
      <RefreshChannelsDropdownItem onRefreshChannels={props.onRefreshChannels} />
    </Dropdown.Menu>
  </Dropdown>
);

const PullChannelButton = (props: { onPullChannel: () => void }) => (
  <IconButton icon={ArrowDownRightCircleFill} label={"Pull..."} onClick={props.onPullChannel} />
);

const RefreshChannelsDropdownItem = (props: { onRefreshChannels: () => void }) => (
  <Dropdown.Item onClick={props.onRefreshChannels}>
    <IconLabel icon={ArrowRepeat} label={"Refresh all channels"} reverse />
  </Dropdown.Item>
);

export { PullChannelActions };
