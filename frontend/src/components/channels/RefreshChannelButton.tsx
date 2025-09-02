import { ArrowClockwise, CheckCircleFill, XOctagonFill } from "react-bootstrap-icons";
import { PullChannelsRequest } from "#/components/events/PullChannelsRequest";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { pullChannelRequested } from "#/store/slices/pullChannels";
import type { ChannelProps } from "#/types";

const RefreshChannelButton = ({ channel }: ChannelProps) => {
  const dispatch = useAppDispatch();
  const pullChannel = useAppSelector((state) => state.pullChannels);

  const isPulledRecently = pullChannel.responses[channel.handle] !== undefined;
  const error = pullChannel.errors[channel.handle];

  const handleClick = () => {
    if (!pullChannel.active) {
      dispatch(pullChannelRequested(channel.handle));
    }
  };

  const icon = error ? (
    <XOctagonFill className={"text-danger"} data-testid={"refreshChannelFailedIcon"} />
  ) : isPulledRecently ? (
    <CheckCircleFill className={"text-success"} data-testid={"refreshChannelSuccessIcon"} />
  ) : (
    <ArrowClockwise
      className={pullChannel.active ? "spin" : "cursor-pointer"}
      data-testid={"refreshChannelIcon"}
      onClick={handleClick}
    />
  );

  return (
    <>
      {icon}
      <PullChannelsRequest />
    </>
  );
};

export { RefreshChannelButton };
