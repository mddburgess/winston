import { EventSourceProvider } from "react-sse-hooks";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import {
    invalidateFetchLimits,
    useFetchRepliesByCommentIdMutation,
} from "#/store/slices/api";
import { fetchedReplies, updateFetchStatus } from "#/store/slices/fetches";
import type { FetchCommentsEvent, FetchStatusEvent } from "#/types";

type FetchRepliesActionProps = {
    commentId: string;
};

export const FetchRepliesAction = ({ commentId }: FetchRepliesActionProps) => {
    const [fetchRepliesByCommentId] = useFetchRepliesByCommentIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetchRepliesByCommentId({ subscriptionId, commentId });
    };

    const handleDataEvent = (event: FetchCommentsEvent) => {
        dispatch(fetchedReplies(event));
        if (event.items.length > 0) {
            const videoId = event.items[0].videoId;
            // dispatch(
            //     commentsApiUtils.updateQueryData(
            //         "listCommentsByVideoId",
            //         videoId,
            //         (draft) => {
            //             const comment = commentsAdapter
            //                 .getSelectors()
            //                 .selectById(draft, commentId);
            //             commentsAdapter.setOne(draft, {
            //                 ...comment,
            //                 replies: repliesAdapter.addMany(
            //                     comment.replies,
            //                     event.items,
            //                 ),
            //             });
            //         },
            //     ),
            // );
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "replies",
                objectId: commentId,
                status: event.status,
            }),
        );
        dispatch(invalidateFetchLimits());
    };

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
            />
        </EventSourceProvider>
    );
};
