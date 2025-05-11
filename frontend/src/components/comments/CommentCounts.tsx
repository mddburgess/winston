import { Col } from "react-bootstrap";
import {
    Chat,
    ChatFill,
    ChatQuote,
    ChatQuoteFill,
} from "react-bootstrap-icons";

type CommentCountsProps = {
    commentsDisabled?: boolean;
    commentCount?: number;
    replyCount?: number;
    totalReplyCount?: number;
    showTotalReplyCount?: boolean;
};

type CommentsColProps = Required<
    Pick<CommentCountsProps, "commentCount" | "commentsDisabled">
>;

type RepliesColProps = Required<
    Pick<
        CommentCountsProps,
        "replyCount" | "totalReplyCount" | "showTotalReplyCount"
    >
>;

const CommentCounts = ({
    commentsDisabled = false,
    commentCount = 0,
    replyCount = 0,
    totalReplyCount = 0,
    showTotalReplyCount = true,
}: CommentCountsProps) => (
    <>
        <CommentsCol
            commentCount={commentCount}
            commentsDisabled={commentsDisabled}
        />
        {commentCount > 0 && (
            <RepliesCol
                replyCount={replyCount}
                totalReplyCount={totalReplyCount}
                showTotalReplyCount={showTotalReplyCount}
            />
        )}
    </>
);

const CommentsCol = ({ commentCount, commentsDisabled }: CommentsColProps) => {
    const CommentsIcon = commentCount > 0 ? ChatFill : Chat;
    return (
        <Col
            className={
                "align-items-center d-flex" +
                (commentsDisabled ? " text-body-tertiary" : "")
            }
            data-testid={"comments"}
            xs={"auto"}
        >
            <CommentsIcon className={"me-2"} data-testid={"commentsIcon"} />
            {commentsDisabled ? "comments disabled" : commentCount}
        </Col>
    );
};

const RepliesCol = ({
    replyCount,
    totalReplyCount,
    showTotalReplyCount,
}: RepliesColProps) => {
    const RepliesIcon =
        replyCount >= totalReplyCount ? ChatQuoteFill : ChatQuote;
    return (
        <Col
            className={"align-items-center d-flex"}
            data-testid={"replies"}
            xs={"auto"}
        >
            <RepliesIcon className={"me-2"} data-testid={"repliesIcon"} />
            {replyCount}
            {showTotalReplyCount && replyCount < totalReplyCount && (
                <span
                    className={"text-body-tertiary"}
                    data-testid="totalReplies"
                >
                    &nbsp;/ {totalReplyCount}
                </span>
            )}
        </Col>
    );
};

export { CommentCounts };
