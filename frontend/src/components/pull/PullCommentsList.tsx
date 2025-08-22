import {
    Col,
    ListGroup,
    ListGroupItem,
    ProgressBar,
    Row,
} from "react-bootstrap";
import { pluralize } from "#/utils";
import type { PullCommentsState } from "#/store/slices/pulls";

type PullCommentsListProps = {
    pullComments: PullCommentsState[];
};

const PullCommentsList = ({ pullComments }: PullCommentsListProps) => (
    <ListGroup variant={"flush"}>
        {pullComments.map((pullComment, index) => (
            <PullCommentsItem
                key={pullComment.video.id}
                pullComment={pullComment}
                index={index}
            />
        ))}
    </ListGroup>
);

const pullCommentsItemVariants = {
    READY: "",
    FETCHING: "info",
    SUCCESSFUL: "success",
    WARNING: "warning",
    FAILED: "danger",
};

type PullCommentsItemProps = {
    pullComment: PullCommentsState;
    index: number;
};

const PullCommentsItem = ({ pullComment, index }: PullCommentsItemProps) => {
    const started = pullComment.status !== "READY";
    const active = pullComment.status === "FETCHING";

    const commentsLabel = pluralize(pullComment.commentIds.length, "comment");
    const repliesLabel = pluralize(
        pullComment.replyIds.length,
        "reply",
        "replies",
    );
    const progressBarLabel = `${commentsLabel} and ${repliesLabel}`;

    switch (pullComment.status) {
        case "READY":
    }

    return (
        <ListGroupItem variant={pullCommentsItemVariants[pullComment.status]}>
            <Row>
                <Col className={"line-clamp-1"}>
                    <strong>{index + 1}.</strong> {pullComment.video.title}
                </Col>
            </Row>
            <Row>
                <Col>
                    <ProgressBar
                        animated={active}
                        label={progressBarLabel}
                        now={started ? 100 : 0}
                        variant={pullCommentsItemVariants[pullComment.status]}
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
