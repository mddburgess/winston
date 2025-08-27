import { render } from "@testing-library/react";
import { CommentLikeCount } from "#/components/comments/CommentLikeCount";
import type { Comment } from "#/api";

describe(CommentLikeCount, () => {
  const comment: Comment = {
    id: "id",
    video_id: "video_id",
    author: {
      id: "author_id",
      handle: "author_handle",
      channel_url: "author_channel",
      profile_image_url: "author_image",
    },
    text: {
      display: "comment text",
      original: "comment text",
    },
    like_count: 1,
    published_at: "2025-01-01T00:00:00Z",
    updated_at: "2025-01-01T00:00:00Z",
    last_fetched_at: "2025-04-01T00:00:00Z",
    properties: {
      important: false,
      hidden: false,
    },
  };

  it("is displayed when like_count > 0", () => {
    const likeCount = render(<CommentLikeCount comment={comment} />).queryByTestId("likeCount");

    expect(likeCount).toBeInTheDocument();
    expect(likeCount).toHaveTextContent("1");
  });

  it("is not displayed when like_count == 0", () => {
    const noLikes: Comment = {
      ...comment,
      like_count: 0,
    };
    const likeCount = render(<CommentLikeCount comment={noLikes} />).queryByTestId("likeCount");

    expect(likeCount).not.toBeInTheDocument();
  });
});
