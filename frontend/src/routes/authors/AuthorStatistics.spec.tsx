import { render } from "@testing-library/react";
import { AuthorStatistics } from "./AuthorStatistics";

describe(AuthorStatistics, () => {
    const author = {
        id: "authorId",
        handle: "@displayName",
        channel_url: "https://www.example.com/@channelUrl",
        profile_image_url: "https://www.example.com/profileImageUrl",
        statistics: {
            video_count: 1,
            comment_count: 2,
            reply_count: 3,
        },
    };

    it("displays the count of videos the author has commented on", () => {
        const result = render(<AuthorStatistics author={author} />);
        const commentedVideos = result.getByTestId("commentedVideos");

        expect(commentedVideos).toBeInTheDocument();
        expect(commentedVideos).toHaveTextContent(
            `${author.statistics.video_count}`,
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
            `${author.statistics.comment_count}`,
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
            `${author.statistics.reply_count}`,
        );

        const icon = totalReplies.firstChild;

        expect(icon).toBeInTheDocument();
        expect(icon).toHaveClass("bi-chat-quote-fill");
    });
});
