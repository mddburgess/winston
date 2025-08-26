import {
    Col,
    ListGroup,
    ListGroupItem,
    ProgressBar,
    Row,
} from "react-bootstrap";
import { pluralize } from "#/utils";
import type {
    PullVideoCommentsState,
    VideoCommentsState,
} from "#/store/slices/pullVideoComments";
import type { VideoProps } from "#/types";

type PullCommentsListProps = {
    state: PullVideoCommentsState;
};

const PullCommentsList = ({ state }: PullCommentsListProps) => (
    <ListGroup variant={"flush"}>
        {state.active.map((video, index) => (
            <PullCommentsItem
                key={video.id}
                video={video}
                state={state.videos[video.id]}
                index={index}
            />
        ))}
    </ListGroup>
);

const pullCommentsItemVariants = {
    ready: "",
    fetching: "info",
    successful: "success",
    warning: "warning",
    failed: "danger",
};

const getOverallStatus = (state: VideoCommentsState) => {
    if (state.comments.status === "successful") {
        return state.replies.status === "ready"
            ? "fetching"
            : state.replies.status;
    }
    return state.comments.status;
};

type PullCommentsItemProps = VideoProps & {
    state: VideoCommentsState;
    index: number;
};

const PullCommentsItem = ({ video, state, index }: PullCommentsItemProps) => {
    const overallStatus = getOverallStatus(state);
    const started = overallStatus !== "ready";
    const active = overallStatus === "fetching";

    const commentsLabel = pluralize(state.comments.items.length, "comment");
    const repliesLabel = pluralize(
        state.replies.items.length,
        "reply",
        "replies",
    );
    const progressBarLabel = `${commentsLabel} and ${repliesLabel}`;

    return (
        <ListGroupItem variant={pullCommentsItemVariants[overallStatus]}>
            <Row>
                <Col className={"line-clamp-1"}>
                    <strong>{index + 1}.</strong> {video.title}
                </Col>
            </Row>
            <Row>
                <Col>
                    <ProgressBar
                        animated={active}
                        label={progressBarLabel}
                        now={started ? 100 : 0}
                        variant={pullCommentsItemVariants[overallStatus]}
                    />
                </Col>
            </Row>
            {/*<Row>*/}
            {/*    <Col xs={"auto"} className={"small ms-auto"}>*/}
            {/*        {pluralize(pullComment.commentIds.length, "comment")}*/}
            {/*    </Col>*/}
            {/*</Row>*/}
        </ListGroupItem>
    );
};

export { PullCommentsList };
