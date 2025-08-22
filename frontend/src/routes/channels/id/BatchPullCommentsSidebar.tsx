import { round } from "lodash";
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
import {
    clearBatchPullComments,
    selectAllPullComments,
} from "#/store/slices/pulls";
import { pluralize } from "#/utils";
import type { ChannelProps } from "#/types";

const BatchPullCommentsSidebar = ({ channel }: ChannelProps) => {
    const dispatch = useAppDispatch();
    const pullState = useAppSelector(
        (state) => state.pulls.batchPullComments[channel.id],
    );
    const pullComments = pullState ? selectAllPullComments(pullState) : [];

    const completed = pullComments.filter((pullComment) =>
        ["SUCCESSFUL", "FAILED"].includes(pullComment.status),
    ).length;

    const handleClose = () => {
        dispatch(clearBatchPullComments(channel.id));
        dispatch(invalidateVideos());
    };

    return (
        <Offcanvas show={pullComments.length > 0} placement={"end"}>
            <Offcanvas.Header>
                <Offcanvas.Title>Pull comments</Offcanvas.Title>
                {pullComments.length > 0 && (
                    <BatchPullCommentsAction
                        channel={channel}
                        pullComments={pullComments}
                    />
                )}
            </Offcanvas.Header>
            <Offcanvas.Body
                className={"bg-primary-subtle height-fit text-primary-emphasis"}
            >
                Pulling comments for{" "}
                <strong>{pluralize(pullComments.length, "video")}</strong>
                <ProgressBar
                    animated={completed < pullComments.length}
                    variant={
                        completed === pullComments.length
                            ? "success"
                            : "primary"
                    }
                    now={completed}
                    max={pullComments.length}
                    label={`${round((completed * 100) / pullComments.length)}%`}
                />
            </Offcanvas.Body>
            <Offcanvas.Body className={"border-bottom border-top p-0"}>
                <Container className={"px-0"}>
                    <PullCommentsList pullComments={pullComments} />
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
