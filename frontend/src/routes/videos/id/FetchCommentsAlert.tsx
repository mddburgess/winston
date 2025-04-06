import {useAppSelector} from "../../../store/hooks";
import {VideoWithChannelDto} from "../../../model/VideoDto";
import {FetchState} from "../../../store/slices/fetches";
import {CommentDto} from "../../../model/CommentDto";
import {Alert, Button, Col} from "react-bootstrap";
import {pluralize} from "../../../utils";
import {CheckCircleFill} from "react-bootstrap-icons";
import {FetchCommentsAction} from "./FetchCommentsAction";

type FetchCommentsAlertProps = {
    video: VideoWithChannelDto
}

type FetchingCommentsAlertProps = FetchCommentsAlertProps & {
    fetchState: FetchState<CommentDto>
}

export const FetchCommentsAlert = ({video}: FetchCommentsAlertProps) => {
    const fetchState = useAppSelector(state => state.fetches.comments[video.id]);
    switch (fetchState?.status) {
        case 'REQUESTED':
        case 'FETCHING':
            return (<FetchingCommentsAlert video={video} fetchState={fetchState} />);
        case 'COMPLETED':
            return (<FetchedCommentsAlert video={video} fetchState={fetchState} />);
        default:
            return (<></>)
    }
}

const FetchingCommentsAlert = ({video, fetchState}: FetchingCommentsAlertProps) => {

    const commentCountLabel = fetchState.data.length === 0
        ? "comments" : pluralize(fetchState.data.length, "comment");

    return (
        <Alert className={"alert-secondary align-items-center d-flex"}>
            <Col>
                Fetching {commentCountLabel} from YouTube...
            </Col>
            <Col xs={"auto"}>
                <Button className={"align-items-center btn-outline-secondary d-flex"} disabled>
                    Fetching...
                    <FetchCommentsAction videoId={video.id}/>
                </Button>
            </Col>
        </Alert>
    );
}

const FetchedCommentsAlert = ({video, fetchState}: FetchingCommentsAlertProps) => (
    <Alert className={"alert-success align-items-center d-flex"}>
        <Col>
            Fetched <strong>{pluralize(fetchState.data.length, "comment")}</strong> from YouTube.
        </Col>
        <Col xs={"auto"}>
            <Button className={"align-items-center btn-outline-success d-flex"} disabled>
                Fetched
                <CheckCircleFill className={"ms-2"}/>
            </Button>
        </Col>
    </Alert>
)
