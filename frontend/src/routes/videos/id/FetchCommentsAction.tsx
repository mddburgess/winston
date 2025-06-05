import { EventSourceProvider } from "react-sse-hooks";
import { useFetchMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { fetchedComments, updateFetchStatus } from "#/store/slices/fetches";
import { markVideoCommentsDisabled } from "#/store/slices/videos";
import type { FetchCommentsEvent, FetchStatusEvent } from "#/types";

type FetchVideosActionProps = {
    videoId: string;
};

export const FetchCommentsAction = ({ videoId }: FetchVideosActionProps) => {
    const [fetch] = useFetchMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetch({
            "X-Notify-Subscription": subscriptionId,
            body: {
                fetch: "comments",
                video_id: videoId,
            },
        });
    };

    const handleDataEvent = (event: FetchCommentsEvent) => {
        // dispatch(
        //     commentsApiUtils.updateQueryData(
        //         "listCommentsByVideoId",
        //         videoId,
        //         (draft) => {
        //             const comments = event.items.map((comment) => ({
        //                 ...comment,
        //                 replies: repliesAdapter.addMany(
        //                     repliesAdapter.getInitialState(),
        //                     comment.replies,
        //                 ),
        //             }));
        //             commentsAdapter.addMany(draft, comments);
        //         },
        //     ),
        // );
        dispatch(fetchedComments(event));
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "comments",
                objectId: videoId,
                status: event.status,
            }),
        );
        if (event.status === "FAILED") {
            if (event.error.type === "/api/problem/comments-disabled") {
                dispatch(markVideoCommentsDisabled(videoId));
            }
        }
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
