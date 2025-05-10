import { Alert, Button, ButtonGroup, Col, Dropdown } from "react-bootstrap";
import {
    ArrowDownRightCircleFill,
    ArrowRepeat,
    CheckCircleFill,
} from "react-bootstrap-icons";
import { Date } from "../../../components/Date";
import { useAppDispatch, useAppSelector } from "../../../store/hooks";
import { requestedVideosForChannelId } from "../../../store/slices/fetches";
import { pluralize } from "../../../utils";
import { FetchVideosAction } from "./FetchVideosAction";
import type { FetchState } from "../../../store/slices/fetches";
import type { ChannelProps } from "../../../types";

type AlertBodyProps = ChannelProps & {
    fetchState: FetchState;
};

const FetchAvailableBody = ({ channel }: ChannelProps) => {
    const dispatch = useAppDispatch();
    return (
        <Alert className={"d-flex align-items-center alert-primary"}>
            <Col>
                The videos for this channel were last fetched from YouTube{" "}
                <strong>
                    <Date date={channel.lastFetchedAt} />.
                </strong>
            </Col>
            <Col xs={"auto"}>
                <Dropdown as={ButtonGroup}>
                    <Button
                        className={`d-flex align-items-center`}
                        onClick={() =>
                            dispatch(
                                requestedVideosForChannelId({
                                    channelId: channel.id,
                                    mode: "LATEST",
                                }),
                            )
                        }
                    >
                        Fetch latest
                        <ArrowDownRightCircleFill className={"ms-2"} />
                    </Button>
                    <Dropdown.Toggle />
                    <Dropdown.Menu align={"end"}>
                        <Dropdown.Item
                            className={"align-items-center d-flex"}
                            onClick={() =>
                                dispatch(
                                    requestedVideosForChannelId({
                                        channelId: channel.id,
                                        mode: "ALL",
                                    }),
                                )
                            }
                        >
                            Fetch all videos
                            <ArrowRepeat className={"ms-2"} />
                        </Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </Col>
        </Alert>
    );
};

const FetchRequestedBody = ({ channel, fetchState }: AlertBodyProps) => {
    const videoCountLabel =
        fetchState.count === 0
            ? "latest videos"
            : pluralize(fetchState.count, "video");

    return (
        <Alert className={"d-flex align-items-center alert-secondary"}>
            <Col>Fetching {videoCountLabel} from YouTube...</Col>
            <Col xs={"auto"}>
                <Button
                    className={
                        "d-flex align-items-center btn-outline-secondary"
                    }
                    disabled={true}
                >
                    Fetching...
                    <FetchVideosAction
                        channelId={channel.id}
                        mode={fetchState.mode ?? "LATEST"}
                    />
                </Button>
            </Col>
        </Alert>
    );
};

const FetchCompletedBody = ({ fetchState }: AlertBodyProps) => (
    <Alert className={"d-flex align-items-center alert-success"}>
        <Col>
            Fetched <strong>{pluralize(fetchState.count, "video")}</strong> from
            YouTube.
        </Col>
        <Col xs={"auto"}>
            <Button
                className={"d-flex align-items-center btn-outline-success"}
                disabled={true}
            >
                Fetched
                <CheckCircleFill className={"ms-2"} />
            </Button>
        </Col>
    </Alert>
);

export const FetchVideosAlert = ({ channel }: ChannelProps) => {
    const fetchState = useAppSelector(
        (state) => state.fetches.videos[channel.id],
    );
    switch (fetchState?.status) {
        case "COMPLETED":
            return (
                <FetchCompletedBody channel={channel} fetchState={fetchState} />
            );
        case "FETCHING":
        case "REQUESTED":
            return (
                <FetchRequestedBody channel={channel} fetchState={fetchState} />
            );
        case "READY":
            return <FetchAvailableBody channel={channel} />;
        default:
            return <></>;
    }
};
