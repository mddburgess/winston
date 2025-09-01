import { Button, Col, Offcanvas, ProgressBar, Row } from "react-bootstrap";
import { RefreshChannelsList } from "#/components/channels/RefreshChannelsList";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { selectAllChannels, useListChannelsQuery } from "#/store/slices/channels";
import { pluralize } from "#/utils";

const RefreshAllChannelsSidebar = (props: { show: boolean; onHide: () => void }) => {
  const { isSuccess, data } = useListChannelsQuery();
  const channels = isSuccess ? selectAllChannels(data) : [];

  return (
    <Offcanvas show={props.show} onHide={props.onHide} placement={"end"}>
      <Offcanvas.Header>
        <Offcanvas.Title>Refresh channels</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body className={"bg-primary-subtle height-fit text-primary-emphasis"}>
        Refreshing <strong>{pluralize(channels.length, "channel")}</strong>
        <ProgressBar />
      </Offcanvas.Body>
      <Offcanvas.Body className={"border-bottom border-top p-0"}>
        <RefreshChannelsList channels={channels} />
      </Offcanvas.Body>
      <Offcanvas.Body className={"bg-body-tertiary height-fit"}>
        <Row className={"flex-center"}>
          <Col>
            <AvailableQuota />
          </Col>
          <Col xs={"auto"} onClick={props.onHide}>
            <Button>Close</Button>
          </Col>
        </Row>
      </Offcanvas.Body>
    </Offcanvas>
  );
};

export { RefreshAllChannelsSidebar };
