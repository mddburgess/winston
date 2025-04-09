import {ListGroup, ListGroupItem, Spinner} from "react-bootstrap";
import {ReplyListItem} from "./ReplyListItem";
import {CommentDto} from "../../model/CommentDto";
import {pluralize} from "../../utils";
import {ReplyAll} from "react-bootstrap-icons";
import {useMemo} from "react";
import {useAppDispatch, useAppSelector} from "../../store/hooks";
import {requestedRepliesForCommentId} from "../../store/slices/fetches";
import {FetchRepliesAction} from "../../routes/videos/id/FetchRepliesAction";

type ReplyListProps = {
    commentId: string;
    totalReplyCount: number,
    replies: CommentDto[],
    highlightAuthorId?: string,
}

export const ReplyList = ({ highlightAuthorId = "", ...props }: ReplyListProps) => {

    const fetchState = useAppSelector(state => state.fetches.replies[props.commentId]);

    const firstReplyIds = props.replies.map(reply => reply.id);
    const moreReplies = useMemo(
        () => (fetchState?.data ?? []).filter(reply => !firstReplyIds.includes(reply.id)),
        [fetchState, firstReplyIds]
    );

    let moreRepliesElement = (<></>);
    if (fetchState?.status === 'COMPLETED') {
        moreRepliesElement = (<ReplyListPart replies={moreReplies} highlightAuthorId={highlightAuthorId} />);
    } else if (fetchState?.status === 'REQUESTED' || fetchState?.status === 'FETCHING') {
        moreRepliesElement = (<FetchingRepliesItem {...props} />);
    } else if (props.totalReplyCount > props.replies.length) {
        moreRepliesElement = (<MoreRepliesItem {...props} />);
    }

    return (
        <ListGroup variant={"flush"} className={"ps-4"}>
            <ReplyListPart {...props} highlightAuthorId={highlightAuthorId} />
            {moreRepliesElement}
        </ListGroup>
    );
}

const ReplyListPart = (props: Partial<ReplyListProps>) => {
    return props.replies?.map((reply) =>
        <ReplyListItem key={reply.id} reply={reply} highlightAuthorId={props.highlightAuthorId}/>
    )
}

const MoreRepliesItem = ({ commentId, totalReplyCount, replies }: ReplyListProps) => {

    const dispatch = useAppDispatch();

    return (
        <ListGroupItem key={"more"} className={"align-items-center d-flex"}>
            <ReplyAll className={"me-2"}/>
            <a className={"small"} onClick={() => dispatch(requestedRepliesForCommentId(commentId))}>
                {pluralize(totalReplyCount - replies.length, "more reply...", "more replies...")}
            </a>
        </ListGroupItem>
    )
}

const FetchingRepliesItem = ({ commentId, totalReplyCount, replies }: ReplyListProps) => {
    return (
        <ListGroupItem key={"fetching"} className={"align-items-center d-flex"}>
            <ReplyAll className={"me-2"}/>
            <span className={"small"}>
                Fetching {pluralize(totalReplyCount - replies.length, "more reply...", "more replies...")}
            </span>
            <FetchRepliesAction commentId={commentId}/>
        </ListGroupItem>
    )
}
