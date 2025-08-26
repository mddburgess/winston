import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { AppEventsSource } from "#/components/events/AppEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { appendReplies } from "#/store/slices/comments";
import { fetchedReplies, updateFetchStatus } from "#/store/slices/fetches";
import { invalidateFetchLimits } from "#/store/slices/limits";
import type { AppEvent, FetchStatusEvent, IdProps } from "#/types";

export const FetchRepliesAction = ({ id: commentId }: IdProps) => {
    const dispatch = useAppDispatch();
    const [pull] = usePullMutation();

    const handleSubscribed = (eventListenerId: string) => {
        void pull({
            body: {
                event_listener_id: eventListenerId,
                operations: [{ pull: "replies", comment_id: commentId }],
            },
        });
    };

    const handleDataEvent = (event: AppEvent) => {
        if (event.replies && event.replies.length > 0) {
            const videoId = event.replies[0].video_id;
            dispatch(appendReplies(videoId, commentId, event.replies));
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
            <AppEventsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
            />
        </EventSourceProvider>
    );
};
