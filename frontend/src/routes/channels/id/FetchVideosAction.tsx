import { EventSourceProvider } from "react-sse-hooks";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import {
    invalidateFetchLimits,
    useFetchVideosByChannelIdMutation,
} from "#/store/slices/api";
import { fetchedVideos, updateFetchStatus } from "#/store/slices/fetches";
import { addVideosForChannel } from "#/store/slices/videos";
import type { Channel } from "#/api";
import type { FetchStatusEvent, FetchVideosEvent } from "#/types";

type FetchVideosWidgetProps = {
    channel: Channel;
    mode: "ALL" | "LATEST";
};

export const FetchVideosAction = ({
    channel,
    mode,
}: FetchVideosWidgetProps) => {
    const [fetchVideosByChannelId] = useFetchVideosByChannelIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetchVideosByChannelId({
            subscriptionId,
            channelId: channel.handle,
            mode,
        });
    };

    const handleDataEvent = (event: FetchVideosEvent) => {
        dispatch(addVideosForChannel(channel.handle, event.items));
        dispatch(fetchedVideos(event));
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "videos",
                objectId: channel.handle,
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
