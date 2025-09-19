import { map } from "lodash";
import { useState } from "react";
import { Col, Row } from "react-bootstrap";
import { ChannelCards } from "#/components/channels/ChannelCards";
import { ChannelListToolbar } from "#/components/channels/ChannelListToolbar";
import { PullChannelModal } from "#/components/channels/PullChannelModal";
import { RefreshChannelsSidebar } from "#/components/channels/RefreshChannelsSidebar";
import { PullChannelsRequest } from "#/components/events/PullChannelsRequest";
import { useAppDispatch } from "#/store/hooks";
import { selectAllChannels, useListChannelsQuery } from "#/store/slices/channels";
import { pullChannelRequested, pullChannelReset } from "#/store/slices/pullChannels";

const ChannelListRoute = () => {
  const dispatch = useAppDispatch();
  const { isSuccess, data } = useListChannelsQuery();
  const channels = isSuccess ? selectAllChannels(data) : [];

  const [showPullChannel, setShowPullChannel] = useState(false);
  const [showRefreshChannels, setShowRefreshChannels] = useState(false);

  const handleShowPullChannel = () => {
    setShowPullChannel(true);
  };

  const handleHidePullChannel = () => {
    setShowPullChannel(false);
  };

  const handleShowRefreshChannels = () => {
    setShowRefreshChannels(true);
    dispatch(pullChannelReset());
    dispatch(pullChannelRequested(map(channels, "handle")));
  };

  const handleHideRefreshChannels = () => {
    setShowRefreshChannels(false);
  };

  return (
    <>
      <Row>
        <Col className={"flex-center"}>
          <h1>Channels</h1>
        </Col>
      </Row>
      <ChannelListToolbar
        channels={channels}
        onPullChannel={handleShowPullChannel}
        onRefreshChannels={handleShowRefreshChannels}
      />
      <ChannelCards channels={channels} />
      <PullChannelModal show={showPullChannel} onHide={handleHidePullChannel} />
      <RefreshChannelsSidebar show={showRefreshChannels} onHide={handleHideRefreshChannels} />
      <PullChannelsRequest />
    </>
  );
};

export { ChannelListRoute };
