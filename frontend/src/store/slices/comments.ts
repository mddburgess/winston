import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { enhancedBackendApi } from "#/store/slices/backend";
import { ascBy } from "#/utils";
import type { Comment, ListCommentsResponse } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";

const commentsApi = enhancedBackendApi.enhanceEndpoints({
    endpoints: {
        listComments: {
            transformResponse: (response: ListCommentsResponse) => {
                const topLevelComments = response.comments.map((comment) => ({
                    ...comment,
                    replies: repliesAdapter.addMany(
                        repliesAdapter.getInitialState(),
                        comment.replies,
                    ),
                }));
                return topLevelCommentsAdapter.addMany(
                    topLevelCommentsAdapter.getInitialState(),
                    topLevelComments,
                );
            },
        },
    },
});

const { useListCommentsQuery } = commentsApi;

const topLevelCommentsAdapter = createEntityAdapter<TopLevelComment>({
    sortComparer: ascBy((topLevelComment) =>
        DateTime.fromISO(topLevelComment.published_at).valueOf(),
    ),
});

const repliesAdapter = createEntityAdapter<Comment>({
    sortComparer: ascBy((comment) =>
        DateTime.fromISO(comment.published_at).valueOf(),
    ),
});

const { selectAll: selectAllTopLevelComments } =
    topLevelCommentsAdapter.getSelectors();

const { selectAll: selectAllReplies, selectTotal: selectReplyCount } =
    repliesAdapter.getSelectors();

export {
    selectAllReplies,
    selectAllTopLevelComments,
    selectReplyCount,
    useListCommentsQuery,
};
