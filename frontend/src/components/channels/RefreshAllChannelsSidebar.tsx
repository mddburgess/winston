import { filter } from "lodash";
import { Button, Col, Offcanvas, Row } from "react-bootstrap";
import { BasicProgressBar } from "#/components/BasicProgressBar";
import { RefreshChannelsList } from "#/components/channels/RefreshChannelsList";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { useAppSelector } from "#/store/hooks";
import { selectAllChannels, useListChannelsQuery } from "#/store/slices/channels";
import { pluralize } from "#/utils";
import type { PullOperationStatus } from "#/types";

const terminalStatuses: PullOperationStatus[] = ["successful", "warning", "failed"];

const RefreshAllChannelsSidebar = (props: { show: boolean; onHide: () => void }) => {
  const { responses, errors } = useAppSelector((state) => state.pullChannels);
  const { isSuccess, data } = useListChannelsQuery();

  const channels = isSuccess ? selectAllChannels(data) : [];
  const completed = filter(responses, (response) => terminalStatuses.includes(response!)).length;

  return (
    <Offcanvas show={props.show} onHide={props.onHide} placement={"end"}>
      <Offcanvas.Header className={"border-bottom"}>
        <Offcanvas.Title>Refresh channels</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body className={"bg-body-tertiary border-bottom height-fit"}>
        Refreshing <strong>{pluralize(channels.length, "channel")}</strong>
        <BasicProgressBar completed={completed} total={channels.length} />
      </Offcanvas.Body>
      <Offcanvas.Body className={"border-bottom border-top p-0"}>
        <RefreshChannelsList channels={channels} responses={responses} errors={errors} />
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
