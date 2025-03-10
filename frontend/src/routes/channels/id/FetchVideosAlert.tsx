import {Alert, Button, Col} from "react-bootstrap";
import {Date} from "../../../components/Date";
import {ArrowDownRightCircleFill, CheckCircleFill} from "react-bootstrap-icons";
import {ChannelDto} from "../../../model/ChannelDto";
import {FetchVideosWidget} from "./FetchVideosWidget";
import {EventSourceProvider} from "react-sse-hooks";
import {useAppDispatch, useAppSelector} from "../../../store/hooks";
import {FetchState, requestedVideosForChannelId} from "../../../store/slices/fetches";

type FetchVideosAlertProps = {
    channel: ChannelDto
}

type AlertBodyProps = FetchVideosAlertProps & {
    fetchState: FetchState
}

export const FetchVideosAlert = ({channel}: FetchVideosAlertProps) => {
    const fetchState = useAppSelector(state => state.fetches[channel.id])
    switch (fetchState?.status) {
        case 'COMPLETED':
            return (<FetchCompletedBody channel={channel} fetchState={fetchState}/>)
        case 'FETCHING':
        case 'REQUESTED':
            return (<FetchRequestedBody channel={channel} fetchState={fetchState}/>)
        case 'READY':
            return (<FetchAvailableBody channel={channel}/>)
        default:
            return (<></>)
    }
}

const FetchAvailableBody = ({channel}: FetchVideosAlertProps) => {
    const dispatch = useAppDispatch();
    return (
        <Alert className={"d-flex align-items-center alert-primary"}>
            <Col>
                The videos for this channel were last fetched from YouTube <strong>
                <Date date={channel.lastFetchedAt}/>.</strong>
            </Col>
            <Col xs={"auto"}>
                <Button
                    className={`d-flex align-items-center`}
                    onClick={() => dispatch(requestedVideosForChannelId(channel.id))}
                >
                    Fetch latest
                    <ArrowDownRightCircleFill className={"ms-2"}/>
                </Button>
            </Col>
        </Alert>
    );
}

const FetchRequestedBody = ({channel, fetchState}: AlertBodyProps) => {

    const videoCountLabel = fetchState.videos.length === 0 ? 'latest videos'
        : fetchState.videos.length === 1 ? '1 video' : fetchState.videos.length + ' videos'

    return (
        <Alert className={"d-flex align-items-center alert-secondary"}>
            <Col>
                Fetching {videoCountLabel} from YouTube...
            </Col>
            <Col xs={"auto"}>
                <Button className={"d-flex align-items-center btn-outline-secondary"} disabled={true}>
                    <EventSourceProvider>
                        Fetching...
                        <FetchVideosWidget channelId={channel.id}/>
                    </EventSourceProvider>
                </Button>
            </Col>
        </Alert>
    );
}

const FetchCompletedBody = ({fetchState}: AlertBodyProps) => {

    const videoCountLabel = fetchState.videos.length === 1 ? '1 video' : fetchState.videos.length + ' videos'

    return (
        <Alert className={"d-flex align-items-center alert-success"}>
            <Col>
                Fetched <strong>{videoCountLabel}</strong> from YouTube.
            </Col>
            <Col xs={"auto"}>
                <Button className={"d-flex align-items-center btn-outline-success"} disabled={true}>
                    Fetched
                    <CheckCircleFill className={"ms-2"}/>
                </Button>
            </Col>
        </Alert>
    )
}
