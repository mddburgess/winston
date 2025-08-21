import { EventSourceProvider } from "react-sse-hooks";
import { useFetchMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { appendReplies } from "#/store/slices/comments";
import { fetchedReplies, updateFetchStatus } from "#/store/slices/fetches";
import type { FetchCommentsEvent, FetchStatusEvent } from "#/types";

type FetchRepliesActionProps = {
    commentId: string;
};

export const FetchRepliesAction = ({ commentId }: FetchRepliesActionProps) => {
    const [fetch] = useFetchMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetch({
            "X-Notify-Subscription": subscriptionId,
            body: {
                fetch: "replies",
                comment_id: commentId,
            },
        });
    };

    const handleDataEvent = (event: FetchCommentsEvent) => {
        if (event.items.length > 0) {
            const videoId = event.items[0].video_id;
            dispatch(appendReplies(videoId, commentId, event.items));
        }
        dispatch(fetchedReplies(event));
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        if (event.status) {
            dispatch(
                updateFetchStatus({
                    fetchType: "replies",
                    objectId: commentId,
                    status: event.status,
                }),
            );
            dispatch(invalidateFetchLimits());
        }
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
