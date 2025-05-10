import { ListGroup, ListGroupItem } from "react-bootstrap";
import { ReplyAll } from "react-bootstrap-icons";
import { FetchRepliesAction } from "../../routes/videos/id/FetchRepliesAction";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { requestedRepliesForId } from "../../store/slices/fetches";
import { pluralize } from "../../utils";
import { ReplyListItem } from "./ReplyListItem";
import type { CommentDto } from "../../model/CommentDto";

type ReplyListProps = {
    commentId: string;
    totalReplyCount: number;
    replies: CommentDto[];
    highlightAuthorId?: string;
};

export const ReplyList = ({
    highlightAuthorId = "",
    ...props
}: ReplyListProps) => {
    const fetchState = useAppSelector(
        (state) => state.fetches.replies[props.commentId],
    );

    let moreRepliesElement = <></>;
    if (
        fetchState?.status === "REQUESTED" ||
        fetchState?.status === "FETCHING"
    ) {
        moreRepliesElement = <FetchingRepliesItem {...props} />;
    } else if (props.totalReplyCount > props.replies.length) {
        moreRepliesElement = <MoreRepliesItem {...props} />;
    }

    return (
        <ListGroup variant={"flush"} className={"ps-4"}>
            <ReplyListPart {...props} highlightAuthorId={highlightAuthorId} />
            {moreRepliesElement}
        </ListGroup>
    );
};

const ReplyListPart = (props: Partial<ReplyListProps>) => {
    return props.replies?.map((reply) => (
        <ReplyListItem
            key={reply.id}
            reply={reply}
            highlightAuthorId={props.highlightAuthorId}
        />
    ));
};

const MoreRepliesItem = ({
    commentId,
    totalReplyCount,
    replies,
}: ReplyListProps) => {
    const dispatch = useAppDispatch();

    return (
        <ListGroupItem key={"more"} className={"align-items-center d-flex"}>
            <ReplyAll className={"me-2"} />
            <a
                className={"small"}
                onClick={() => dispatch(requestedRepliesForId(commentId))}
            >
                {pluralize(
                    totalReplyCount - replies.length,
                    "more reply...",
                    "more replies...",
                )}
            </a>
        </ListGroupItem>
    );
};

const FetchingRepliesItem = ({
    commentId,
    totalReplyCount,
    replies,
}: ReplyListProps) => {
    return (
        <ListGroupItem key={"fetching"} className={"align-items-center d-flex"}>
            <ReplyAll className={"me-2"} />
            <span className={"small"}>
                Fetching{" "}
                {pluralize(
                    totalReplyCount - replies.length,
                    "more reply...",
                    "more replies...",
                )}
            </span>
            <FetchRepliesAction commentId={commentId} />
        </ListGroupItem>
    );
};
