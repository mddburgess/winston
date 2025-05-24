import { render } from "@testing-library/react";
import { DateTime } from "luxon";
import { CommentCounts } from "./CommentCounts";

describe(CommentCounts, () => {
    describe("the comment count", () => {
        it("is displayed when the commentsDisabled property is false", () => {
            const comments = render(<CommentCounts />).getByTestId("comments");

            expect(comments).toBeInTheDocument();
            expect(comments).toHaveTextContent("0");
        });

        it("displays a message when the commentsDisabled property is true", () => {
            const comments = render(
                <CommentCounts commentsDisabled={true} />,
            ).getByTestId("comments");

            expect(comments).toBeInTheDocument();
            expect(comments).toHaveClass("text-body-tertiary");
            expect(comments).toHaveTextContent("comments disabled");
        });
    });

    describe("the comment icon", () => {
        it("is unfilled when the last fetched at date is undefined", () => {
            const commentsIcon = render(<CommentCounts />).getByTestId(
                "commentsIcon",
            );

            expect(commentsIcon).toBeInTheDocument();
            expect(commentsIcon).toHaveClass("bi-chat");
        });

        it("is filled when the last fetched at date is set", () => {
            const commentsIcon = render(
                <CommentCounts lastFetchedAt={DateTime.now().toISO()} />,
            ).getByTestId("commentsIcon");

            expect(commentsIcon).toBeInTheDocument();
            expect(commentsIcon).toHaveClass("bi-chat-fill");
        });
    });

    describe("the reply count", () => {
        it("is displayed when the comment count is greater than 0", () => {
            const replies = render(
                <CommentCounts commentCount={1} />,
            ).getByTestId("replies");

            expect(replies).toBeInTheDocument();
            expect(replies).toHaveTextContent("0");
        });

        it("is not displayed when the comment count is 0", () => {
            const replies = render(<CommentCounts />).queryByTestId("replies");

            expect(replies).not.toBeInTheDocument();
        });
    });

    describe("the total reply count", () => {
        it("is displayed when it is greater than the reply count", () => {
            const totalReplies = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={2}
                    totalReplyCount={3}
                />,
            ).getByTestId("totalReplies");

            expect(totalReplies).toBeInTheDocument();
            expect(totalReplies).toHaveClass("text-body-tertiary");
            expect(totalReplies).toContainHTML("&nbsp;/ 3");
        });

        it("is not displayed when it is equal to the reply count", () => {
            const totalReplies = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={2}
                    totalReplyCount={2}
                />,
            ).queryByTestId("totalReplies");

            expect(totalReplies).not.toBeInTheDocument();
        });

        it("is not displayed when it is less than the reply count", () => {
            const totalReplies = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={3}
                    totalReplyCount={2}
                />,
            ).queryByTestId("totalReplies");

            expect(totalReplies).not.toBeInTheDocument();
        });

        it("is not displayed when the showTotalReplies property is false", () => {
            const totalReplies = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={2}
                    totalReplyCount={3}
                    showTotalReplyCount={false}
                />,
            ).queryByTestId("totalReplies");

            expect(totalReplies).not.toBeInTheDocument();
        });
    });

    describe("the reply icon", () => {
        it("is unfilled when the reply count is less than the total reply count", () => {
            const repliesIcon = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={2}
                    totalReplyCount={3}
                />,
            ).getByTestId("repliesIcon");

            expect(repliesIcon).toBeInTheDocument();
            expect(repliesIcon).toHaveClass("bi-chat-quote");
        });

        it("is filled when the reply count is equal to the total reply count", () => {
            const result = render(
                <CommentCounts
                    commentCount={1}
                    replyCount={2}
                    totalReplyCount={2}
                />,
            );
            const repliesIcon = result.getByTestId("repliesIcon");

            expect(repliesIcon).toBeInTheDocument();
            expect(repliesIcon).toHaveClass("bi-chat-quote-fill");
        });
    });
});
