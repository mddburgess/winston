import {Alert, Button, Col} from "react-bootstrap";
import {Date} from "../../../components/Date";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";
import {ChannelDto} from "../../../model/ChannelDto";

type FetchVideosAlertProps = {
    channel: ChannelDto
}

export const FetchVideosAlert = ({channel}: FetchVideosAlertProps) => (
    <Alert className={"d-flex align-items-center py-2 pe-2"}>
        <Col>
            The videos for this channel were last fetched from YouTube&nbsp;
            <strong>{channel.lastFetchedAt && <Date date={channel.lastFetchedAt}></Date>}.</strong>
        </Col>
        <Col xs={"auto"}>
            <Button className={"d-flex align-items-center"}>
                Fetch latest
                <ArrowDownRightCircleFill className={"ms-2"}/>
            </Button>
        </Col>
    </Alert>
)
