import { filter, round } from "lodash";
import {
    Button,
    Col,
    Container,
    Offcanvas,
    ProgressBar,
    Row,
} from "react-bootstrap";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { PullCommentsList } from "#/components/pull/PullCommentsList";
import { BatchPullCommentsAction } from "#/routes/channels/id/BatchPullCommentsAction";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateVideos } from "#/store/slices/backend";
import { clearPullComments } from "#/store/slices/pullVideoComments";
import { pluralize } from "#/utils";
import type { PullOperationRead } from "#/api";
import type { VideoCommentsState } from "#/store/slices/pullVideoComments";

const terminalStatuses: PullOperationRead["status"][] = [
    "successful",
    "warning",
    "failed",
];

const getOverallStatus = (state: VideoCommentsState) => {
    if (state.comments.status === "successful") {
        return state.replies.status === "ready"
            ? "fetching"
            : state.replies.status;
    }
    return state.comments.status;
};

const BatchPullCommentsSidebar = () => {
    const dispatch = useAppDispatch();

    const pullVideoComments = useAppSelector(
        (state) => state.pullVideoComments,
    );

    const activeVideos = pullVideoComments.active;
    const isActive = activeVideos.length > 0;
    const completed = filter(pullVideoComments.videos, (videoState) =>
        terminalStatuses.includes(getOverallStatus(videoState)),
    ).length;

    const handleClose = () => {
        dispatch(clearPullComments());
        dispatch(invalidateVideos());
    };

    return (
        <Offcanvas show={isActive} placement={"end"}>
            <Offcanvas.Header>
                <Offcanvas.Title>Pull comments</Offcanvas.Title>
                {isActive && <BatchPullCommentsAction videos={activeVideos} />}
            </Offcanvas.Header>
            <Offcanvas.Body
                className={"bg-primary-subtle height-fit text-primary-emphasis"}
            >
                Pulling comments for{" "}
                <strong>{pluralize(activeVideos.length, "video")}</strong>
                <ProgressBar
                    animated={completed < activeVideos.length}
                    variant={
                        completed === activeVideos.length
                            ? "success"
                            : "primary"
                    }
                    now={completed}
                    max={activeVideos.length}
                    label={`${round((completed * 100) / activeVideos.length)}%`}
                />
            </Offcanvas.Body>
            <Offcanvas.Body className={"border-bottom border-top p-0"}>
                <Container className={"px-0"}>
                    <PullCommentsList state={pullVideoComments} />
                </Container>
            </Offcanvas.Body>
            <Offcanvas.Body className={"bg-body-tertiary height-fit"}>
                <Row className={"flex-center"}>
                    <Col>
                        <AvailableQuota />
                    </Col>
                    <Col xs={"auto"}>
                        <Button onClick={handleClose}>Close</Button>
                    </Col>
                </Row>
            </Offcanvas.Body>
        </Offcanvas>
    );
};

export { BatchPullCommentsSidebar };
