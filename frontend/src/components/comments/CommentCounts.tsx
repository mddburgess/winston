import {Col} from "react-bootstrap";
import {Chat, ChatFill, ChatQuote, ChatQuoteFill} from "react-bootstrap-icons";

type CommentCountsProps = {
    comments: number;
    commentsDisabled?: boolean;
    replies: number;
    totalReplies: number;
    showTotalReplies?: boolean;
}
type CommentsColProps = Pick<CommentCountsProps, 'comments' | 'commentsDisabled'>;
type RepliesColProps = Pick<CommentCountsProps, 'replies' | 'totalReplies' | 'showTotalReplies'>

export const CommentCounts = ({
    comments,
    commentsDisabled = false,
    replies,
    totalReplies,
    showTotalReplies = true,
}: CommentCountsProps) => (
    <>
        <CommentsCol
            comments={comments}
            commentsDisabled={commentsDisabled}
        />
        {comments > 0 &&
            <RepliesCol
                replies={replies}
                totalReplies={totalReplies}
                showTotalReplies={showTotalReplies}
            />
        }
    </>
)

const CommentsCol = ({ comments, commentsDisabled }: CommentsColProps) => {
    const CommentsIcon = comments > 0 ? ChatFill : Chat;
    return (
        <Col
            className={"align-items-center d-flex" + (commentsDisabled ? " text-body-tertiary" : "")}
            data-testid={"comments"}
            xs={"auto"}
        >
            <CommentsIcon
                className={"me-2"}
                data-testid={"commentsIcon"}
            />
            {commentsDisabled ? 'comments disabled' : comments}
        </Col>
    );
};

const RepliesCol = ({ replies, totalReplies, showTotalReplies } : RepliesColProps) => {
    const RepliesIcon = replies >= totalReplies ? ChatQuoteFill : ChatQuote;
    return (
        <Col
            className={"align-items-center d-flex"}
            data-testid={"replies"}
            xs={"auto"}
        >
            <RepliesIcon
                className={"me-2"}
                data-testid={"repliesIcon"}
            />
            {replies}
            {showTotalReplies && replies < totalReplies &&
                <span className={"text-body-tertiary"} data-testid = "totalReplies">
                    &nbsp;/ {totalReplies}
                </span>
            }
        </Col>
    )
}
