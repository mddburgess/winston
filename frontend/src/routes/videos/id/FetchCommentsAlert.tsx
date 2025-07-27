import { Alert, Button, Col } from "react-bootstrap";
import { CheckCircleFill } from "react-bootstrap-icons";
import { PullCommentsAndRepliesAction } from "#/routes/videos/id/PullCommentsAndRepliesAction";
import { useAppSelector } from "#/store/hooks";
import { pluralize } from "#/utils";
import type { Video } from "#/api";
import type { FetchState } from "#/store/slices/fetches";

type Props = {
    video: Video;
};

type FetchingCommentsAlertProps = Props & {
    fetchState: FetchState;
};

const FetchingCommentsAlert = ({
    video,
    fetchState,
}: FetchingCommentsAlertProps) => (
    <Alert className={"alert-secondary align-items-center d-flex"}>
        <Col>
            Fetching {pluralize(fetchState.count, "comment")} from YouTube...
        </Col>
        <Col xs={"auto"}>
            <Button
                className={"align-items-center btn-outline-secondary d-flex"}
                disabled
            >
                Fetching...
                <PullCommentsAndRepliesAction video={video} />
            </Button>
        </Col>
    </Alert>
);

const FetchedCommentsAlert = ({ fetchState }: FetchingCommentsAlertProps) => (
    <Alert className={"alert-success align-items-center d-flex"}>
        <Col>
            Fetched <strong>{pluralize(fetchState.count, "comment")}</strong>{" "}
            from YouTube.
        </Col>
        <Col xs={"auto"}>
            <Button
                className={"align-items-center btn-outline-success d-flex"}
                disabled
            >
                Fetched
                <CheckCircleFill className={"ms-2"} />
            </Button>
        </Col>
    </Alert>
);

export const FetchCommentsAlert = ({ video }: Props) => {
    const fetchState = useAppSelector(
        (state) => state.fetches.comments[video.id],
    );
    switch (fetchState?.status) {
        case "REQUESTED":
        case "FETCHING":
            return (
                <FetchingCommentsAlert video={video} fetchState={fetchState} />
            );
        case "COMPLETED":
            return (
                <FetchedCommentsAlert video={video} fetchState={fetchState} />
            );
        default:
            return <></>;
    }
};
