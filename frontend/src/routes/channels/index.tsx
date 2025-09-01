import { map } from "lodash";
import { useState } from "react";
import { Col, Row } from "react-bootstrap";
import { PullChannelActions } from "#/components/channels/PullChannelActions";
import { PullChannelModal } from "#/components/channels/PullChannelModal";
import { RefreshAllChannelsSidebar } from "#/components/channels/RefreshAllChannelsSidebar";
import { PullChannelsRequest } from "#/components/events/PullChannelsRequest";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { ChannelCards } from "#/routes/channels/ChannelCards";
import { useAppDispatch } from "#/store/hooks";
import { selectAllChannels, useListChannelsQuery } from "#/store/slices/channels";
import { pullChannelRequested } from "#/store/slices/pullChannels";

const ChannelListRoute = () => {
  const dispatch = useAppDispatch();
  const { isSuccess, data } = useListChannelsQuery();
  const channels = isSuccess ? selectAllChannels(data) : [];

  const [showPullChannel, setShowPullChannel] = useState(false);
  const [showRefreshAllChannels, setShowRefreshAllChannels] = useState(false);

  const handleRefreshAllChannels = () => {
    setShowRefreshAllChannels(true);
    dispatch(pullChannelRequested(map(channels, "handle")));
  };

  return (
    <>
      <Row className={"mb-2"}>
        <Col className={"flex-center"}>
          <p className={"h1 m-0"}>Channels</p>
        </Col>
        <Col xs={"auto"} className={"flex-center"}>
          <PullChannelActions
            channels={channels}
            onPullChannel={() => setShowPullChannel(true)}
            onRefreshAllChannels={handleRefreshAllChannels}
          />
        </Col>
      </Row>
      <PaginationContext pageSize={12} items={channels}>
        {({ pageNumber, setPageNumber, pageSize, pageItems, totalItemCount }) => (
          <>
            <PaginationRow
              name={"channel"}
              total={totalItemCount}
              pageSize={pageSize}
              page={pageNumber}
              setPage={setPageNumber}
            />
            <ChannelCards channels={pageItems} />
          </>
        )}
      </PaginationContext>
      <PullChannelModal show={showPullChannel} onHide={() => setShowPullChannel(false)} />
      <RefreshAllChannelsSidebar show={showRefreshAllChannels} onHide={() => setShowRefreshAllChannels(false)} />
      <PullChannelsRequest />
    </>
  );
};

export { ChannelListRoute };
