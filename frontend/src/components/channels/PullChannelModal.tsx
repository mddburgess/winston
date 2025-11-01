import { map } from "lodash";
import { useState } from "react";
import { Button, Col, Form, Modal, Row } from "react-bootstrap";
import { ArrowDownRightCircleFill, BoxArrowInUpRight, ExclamationDiamond, InfoCircleFill } from "react-bootstrap-icons";
import { useNavigate } from "react-router";
import { ChannelInput } from "#/components/channels/ChannelInput";
import { IconAlert } from "#/components/IconAlert";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { selectAllChannels, useListChannelsQuery } from "#/store/slices/channels";
import { pullChannelRequested } from "#/store/slices/pullChannels";
import { routes } from "#/utils/links";
import type { FormEvent } from "react";
import type { ButtonProps } from "react-bootstrap";

const PullChannelModal = (props: { show: boolean; onHide: () => void }) => {
  const dispatch = useAppDispatch();
  const { active, errors } = useAppSelector((state) => state.pullChannels);

  const { isSuccess, data } = useListChannelsQuery({});
  const channels = isSuccess ? map(selectAllChannels(data), "handle") : [];

  const navigate = useNavigate();
  const [channelHandle, setChannelHandle] = useState("");
  const [lastRequested, setLastRequested] = useState("");

  const isBlank = channelHandle === "";
  const isLastRequested = channelHandle === lastRequested;
  const isAlreadyPulled = channels.includes(`@${channelHandle.toLowerCase()}`);
  const isPullError = errors[channelHandle] !== undefined;
  const mayPull = !(isBlank || isLastRequested || isAlreadyPulled || isPullError);

  const handleShow = () => {
    setChannelHandle("");
    setLastRequested("");
  };

  const handleSubmit = () => {
    if (isAlreadyPulled) {
      navigateToChannel();
    } else if (mayPull) {
      dispatch(pullChannelRequested(channelHandle));
      setLastRequested(channelHandle);
    }
  };

  const navigateToChannel = () => {
    void navigate(routes.channels.details(`@${channelHandle}`));
  };

  return (
    <Modal show={props.show} onShow={handleShow} onHide={props.onHide}>
      <Modal.Header closeButton>
        <Modal.Title>Pull channel</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <PullChannelForm
          channelHandle={channelHandle}
          disabled={active}
          onChange={setChannelHandle}
          onSubmit={handleSubmit}
        />
        {isAlreadyPulled && !isLastRequested && (
          <IconAlert variant={"info"} icon={InfoCircleFill} label={"This channel has already been pulled."} />
        )}
        {isPullError && (
          <IconAlert variant={"danger"} icon={ExclamationDiamond} label={errors[channelHandle]?.detail} />
        )}
      </Modal.Body>
      <Modal.Footer>
        <CancelButton onClick={props.onHide} />
        {isAlreadyPulled ? (
          <OpenChannelButton onClick={navigateToChannel} />
        ) : (
          <PullChannelButton disabled={!mayPull} onClick={handleSubmit} />
        )}
      </Modal.Footer>
    </Modal>
  );
};

const PullChannelForm = (props: {
  disabled: boolean;
  channelHandle: string;
  onChange: (channelHandle: string) => void;
  onSubmit: () => void;
}) => {
  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    props.onSubmit();
  };

  return (
    <Row>
      <Col>
        <Form onSubmit={handleSubmit}>
          <Form.Group>
            <Form.Label>YouTube channel handle or URL</Form.Label>
            <ChannelInput value={props.channelHandle} onChange={props.onChange} disabled={props.disabled} />
          </Form.Group>
        </Form>
      </Col>
    </Row>
  );
};

const CancelButton = (props: ButtonProps) => (
  <Button {...props} variant={"outline-secondary"}>
    Cancel
  </Button>
);

const PullChannelButton = (props: ButtonProps) => (
  <IconButton {...props} icon={ArrowDownRightCircleFill} label={"Pull"} />
);

const OpenChannelButton = (props: ButtonProps) => (
  <IconButton {...props} icon={BoxArrowInUpRight} label={"Open channel"} />
);

export { PullChannelModal };
