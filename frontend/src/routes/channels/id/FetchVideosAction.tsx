import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { AppEventsSource } from "#/components/events/AppEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { fetchedVideos, updateFetchStatus } from "#/store/slices/fetches";
import { appendVideos } from "#/store/slices/videos";
import type { Channel } from "#/api";
import type { AppEvent, FetchStatusEvent } from "#/types";

type FetchVideosWidgetProps = {
    channel: Channel;
    mode: "all" | "latest";
};

export const FetchVideosAction = ({
    channel,
    mode,
}: FetchVideosWidgetProps) => {
    const dispatch = useAppDispatch();
    const [pull] = usePullMutation();

    const handleSubscribed = (eventListenerId: string) => {
        void pull({
            body: {
                event_listener_id: eventListenerId,
                operations: [
                    {
                        pull: "videos",
                        channel_handle: channel.handle,
                        range: mode,
                    },
                ],
            },
        });
    };

    const handleDataEvent = (event: AppEvent) => {
        dispatch(appendVideos(channel.handle, event.videos ?? []));
        dispatch(fetchedVideos(event));
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        if (event.status) {
            dispatch(
                updateFetchStatus({
                    fetchType: "videos",
                    objectId: channel.id,
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
