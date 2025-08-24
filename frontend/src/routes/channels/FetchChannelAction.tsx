import { useNavigate } from "react-router";
import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { AppEventsSource } from "#/components/events/AppEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { appendChannels } from "#/store/slices/channels";
import { updateFetchStatus } from "#/store/slices/fetches";
import { routes } from "#/utils/links";
import type { AppEvent, FetchStatusEvent } from "#/types";

type FetchChannelActionProps = {
    channelHandle: string;
};

export const FetchChannelAction = ({
    channelHandle,
}: FetchChannelActionProps) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [pull] = usePullMutation();

    const handleSubscribed = (eventListenerId: string) => {
        void pull({
            body: {
                event_listener_id: eventListenerId,
                operations: [
                    { pull: "channel", channel_handle: channelHandle },
                ],
            },
        });
    };

    const handleDataEvent = (event: AppEvent) => {
        dispatch(invalidateFetchLimits());
        if (event.channel) {
            dispatch(appendChannels([event.channel]));
            void navigate(routes.channels.details(event.channel.handle));
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        if (event.status) {
            dispatch(
                updateFetchStatus({
                    fetchType: "channel",
                    objectId: channelHandle,
                    status: event.status,
                }),
            );
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
