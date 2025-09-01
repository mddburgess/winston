import { isEmpty } from "lodash";
import { ButtonGroup, Dropdown } from "react-bootstrap";
import { ArrowDownRightCircleFill, ArrowRepeat } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { IconLabel } from "#/components/IconLabel";
import type { ChannelListProps } from "#/types";

type PullChannelActionsProps = ChannelListProps & {
  onPullChannel: () => void;
  onRefreshAllChannels: () => void;
};

const PullChannelActions = (props: PullChannelActionsProps) => {
  return isEmpty(props.channels) ? (
    <PullChannelButton onPullChannel={props.onPullChannel} />
  ) : (
    <PullChannelDropdown {...props} />
  );
};

const PullChannelDropdown = (props: PullChannelActionsProps) => {
  return (
    <Dropdown as={ButtonGroup}>
      <PullChannelButton onPullChannel={props.onPullChannel} />
      <Dropdown.Toggle />
      <Dropdown.Menu align={"end"}>
        <RefreshAllChannelsDropdownItem onRefreshAllChannels={props.onRefreshAllChannels} />
      </Dropdown.Menu>
    </Dropdown>
  );
};

const PullChannelButton = (props: { onPullChannel: () => void }) => (
  <IconButton icon={ArrowDownRightCircleFill} label={"Pull..."} onClick={props.onPullChannel} />
);

const RefreshAllChannelsDropdownItem = (props: { onRefreshAllChannels: () => void }) => (
  <Dropdown.Item onClick={props.onRefreshAllChannels}>
    <IconLabel icon={ArrowRepeat} label={"Refresh all channels"} reverse />
  </Dropdown.Item>
);

export { PullChannelActions };
