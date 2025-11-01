import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { enhancedBackendApi } from "#/store/slices/backend";
import { ascBy } from "#/utils";
import type { Comment, ListCommentsResp } from "#/api";
import type { CommentState } from "#/store/slices/backend";
import type { TopLevelComment } from "#/types";

const commentsApi = enhancedBackendApi.enhanceEndpoints({
  endpoints: {
    listComments: {
      transformResponse: (response: ListCommentsResp) =>
        topLevelCommentsAdapter.addMany(
          topLevelCommentsAdapter.getInitialState(),
          transformComments(response.comments),
        ),
    },
  },
});

const { useListCommentsQuery } = commentsApi;

const topLevelCommentsAdapter = createEntityAdapter<CommentState>({
  sortComparer: ascBy((topLevelComment) => DateTime.fromISO(topLevelComment.published_at).valueOf()),
});

const repliesAdapter = createEntityAdapter<Comment>({
  sortComparer: ascBy((comment) => DateTime.fromISO(comment.published_at).valueOf()),
});

const transformComments = (comments: TopLevelComment[]) => {
  return comments.map((comment) => ({
    ...comment,
    replies: repliesAdapter.addMany(repliesAdapter.getInitialState(), comment.replies ?? []),
  }));
};

const { selectAll: selectAllTopLevelComments } = topLevelCommentsAdapter.getSelectors();

const { selectAll: selectAllReplies, selectTotal: selectReplyCount } = repliesAdapter.getSelectors();

export { selectAllReplies, selectAllTopLevelComments, selectReplyCount, useListCommentsQuery };
