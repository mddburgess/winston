import { render } from "@testing-library/react";
import { AuthorStatistics } from "./AuthorStatistics";
import type { AuthorWithStatistics } from "../../types";

describe("AuthorStatistics", () => {
    const author: AuthorWithStatistics = {
        id: "authorId",
        displayName: "@displayName",
        channelUrl: "https://www.example.com/@channelUrl",
        profileImageUrl: "https://www.example.com/profileImageUrl",
        statistics: {
            commentedVideos: 1,
            totalComments: 2,
            totalReplies: 3,
        },
    };

    it("displays the count of videos the author has commented on", () => {
        const result = render(<AuthorStatistics author={author} />);

        const commentedVideos = result.getByTestId("commentedVideos");
        expect(commentedVideos).toBeInTheDocument();
        expect(commentedVideos).toHaveTextContent(
            `${author.statistics.commentedVideos}`,
        );

        const icon = commentedVideos.firstChild;
        expect(icon).toBeInTheDocument();
        expect(icon).toHaveClass("bi-youtube");
    });

    it("displays the total number of comments the author has made", () => {
        const result = render(<AuthorStatistics author={author} />);

        const totalComments = result.getByTestId("totalComments");
        expect(totalComments).toBeInTheDocument();
        expect(totalComments).toHaveTextContent(
            `${author.statistics.totalComments}`,
        );

        const icon = totalComments.firstChild;
        expect(icon).toBeInTheDocument();
        expect(icon).toHaveClass("bi-chat-fill");
    });

    it("displays the total number of replies the author has made", () => {
        const result = render(<AuthorStatistics author={author} />);

        const totalReplies = result.getByTestId("totalReplies");
        expect(totalReplies).toBeInTheDocument();
        expect(totalReplies).toHaveTextContent(
            `${author.statistics.totalReplies}`,
        );

        const icon = totalReplies.firstChild;
        expect(icon).toBeInTheDocument();
        expect(icon).toHaveClass("bi-chat-quote-fill");
    });
});
