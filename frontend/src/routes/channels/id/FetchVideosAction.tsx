import { EventSourceProvider } from "react-sse-hooks";
import { useFetchMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { fetchedVideos, updateFetchStatus } from "#/store/slices/fetches";
import { appendVideos } from "#/store/slices/videos";
import type { Channel } from "#/api";
import type { FetchStatusEvent, FetchVideosEvent } from "#/types";

type FetchVideosWidgetProps = {
    channel: Channel;
    mode: "all" | "latest";
};

export const FetchVideosAction = ({
    channel,
    mode,
}: FetchVideosWidgetProps) => {
    const [fetch] = useFetchMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetch({
            "X-Notify-Subscription": subscriptionId,
            body: {
                fetch: "videos",
                channel_id: channel.id,
                range: mode,
            },
        });
    };

    const handleDataEvent = (event: FetchVideosEvent) => {
        dispatch(appendVideos(channel.handle, event.items));
        dispatch(fetchedVideos(event));
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "videos",
                objectId: channel.id,
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
