import {render} from "@testing-library/react";
import {CommentCounts} from "./CommentCounts";

describe('CommentCounts', () => {

    describe("the comment count", () => {

        it("is displayed when the commentsDisabled property is false", () => {
            const result = render(
                <CommentCounts comments={0} replies={0} totalReplies={0}/>
            )

            const comments = result.getByTestId("comments");
            expect(comments).toBeInTheDocument();
            expect(comments).toHaveTextContent("0");
        })

        it("displays a message when the commentsDisabled property is true", () => {
            const result = render(
                <CommentCounts comments={0} commentsDisabled={true} replies={0} totalReplies={0}/>
            )

            const comments = result.getByTestId("comments");
            expect(comments).toBeInTheDocument();
            expect(comments).toHaveClass("text-body-tertiary");
            expect(comments).toHaveTextContent("comments disabled");
        })
    })

    describe("the comment icon", () => {

        it("is unfilled when the comment count is 0", () => {
            const result = render(
                <CommentCounts comments={0} replies={0} totalReplies={0}/>
            )

            const commentsIcon = result.getByTestId("commentsIcon");
            expect(commentsIcon).toBeInTheDocument();
            expect(commentsIcon).toHaveClass("bi-chat");
        })

        it("is filled when the comment count is greater than 0", () => {
            const result = render(
                <CommentCounts comments={1} replies={0} totalReplies={0}/>
            )

            const commentsIcon = result.getByTestId("commentsIcon");
            expect(commentsIcon).toBeInTheDocument();
            expect(commentsIcon).toHaveClass("bi-chat-fill");
        })
    })

    describe("the reply count", () => {

        it("is displayed when the comment count is greater than 0", () => {
            const result = render(
                <CommentCounts comments={1} replies={0} totalReplies={0}/>
            )

            const replies = result.getByTestId("replies");
            expect(replies).toBeInTheDocument();
            expect(replies).toHaveTextContent("0");
        })

        it("is not displayed when the comment count is 0", () => {
            const result = render(
                <CommentCounts comments={0} replies={0} totalReplies={0}/>
            )

            const replies = result.queryByTestId("replies");
            expect(replies).not.toBeInTheDocument();
        })
    })

    describe("the total reply count", () => {

        it("is displayed when it is greater than the reply count", () => {
            const result = render(
                <CommentCounts comments={1} replies={2} totalReplies={3}/>
            );

            const totalReplies = result.getByTestId("totalReplies");
            expect(totalReplies).toBeInTheDocument();
            expect(totalReplies).toHaveClass("text-body-tertiary");
            expect(totalReplies).toContainHTML("&nbsp;/ 3");
        });

        it("is not displayed when it is equal to the reply count", () => {
            const result = render(
                <CommentCounts comments={1} replies={2} totalReplies={2}/>
            )

            const totalReplies = result.queryByTestId("totalReplies");
            expect(totalReplies).not.toBeInTheDocument()
        });

        it("is not displayed when it is less than the reply count", () => {
            const result = render(
                <CommentCounts comments={1} replies={3} totalReplies={2}/>
            )

            const totalReplies = result.queryByTestId("totalReplies")
            expect(totalReplies).not.toBeInTheDocument()
        })

        it("is not displayed when the showTotalReplies property is false", () => {
            const result = render(
                <CommentCounts comments={1} replies={2} totalReplies={3} showTotalReplies={false}/>
            )

            const totalReplies = result.queryByTestId("totalReplies")
            expect(totalReplies).not.toBeInTheDocument()
        })
    })

    describe("the reply icon", () => {

        it("is unfilled when the reply count is less than the total reply count", () => {
            const result = render(
                <CommentCounts comments={1} replies={2} totalReplies={3}/>
            )

            const repliesIcon = result.getByTestId("repliesIcon");
            expect(repliesIcon).toBeInTheDocument();
            expect(repliesIcon).toHaveClass("bi-chat-quote");
        })

        it("is filled when the reply count is equal to the total reply count", () => {
            const result = render(
                <CommentCounts comments={1} replies={2} totalReplies={2}/>
            )

            const repliesIcon = result.getByTestId("repliesIcon");
            expect(repliesIcon).toBeInTheDocument();
            expect(repliesIcon).toHaveClass("bi-chat-quote-fill");
        })
    })
})
