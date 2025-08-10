import { EventSourceProvider } from "react-sse-hooks";
import { useFetchMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { appendReplies } from "#/store/slices/comments";
import { updateFetchStatus } from "#/store/slices/fetches";
import type { FetchCommentsEvent, FetchStatusEvent } from "#/types";

type FetchRepliesActionProps = {
    videoId: string;
};

export const FetchVideoRepliesAction = ({
    videoId,
}: FetchRepliesActionProps) => {
    const [fetch] = useFetchMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetch({
            "X-Notify-Subscription": subscriptionId,
            body: {
                fetch: "replies",
                video_id: videoId,
            },
        });
    };

    const handleDataEvent = (event: FetchCommentsEvent) => {
        if (event.items.length > 0) {
            const commentId = event.objectId;
            dispatch(appendReplies(videoId, commentId, event.items));
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        if (event.status) {
            dispatch(
                updateFetchStatus({
                    fetchType: "replies",
                    objectId: videoId,
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
