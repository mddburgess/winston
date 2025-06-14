import { useNavigate } from "react-router";
import { EventSourceProvider } from "react-sse-hooks";
import { useFetchMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { appendChannels } from "#/store/slices/channels";
import { updateFetchStatus } from "#/store/slices/fetches";
import { routes } from "#/utils/links";
import type { FetchChannelEvent, FetchStatusEvent } from "#/types";

type FetchChannelActionProps = {
    channelHandle: string;
};

export const FetchChannelAction = ({
    channelHandle,
}: FetchChannelActionProps) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [fetch] = useFetchMutation();

    const handleSubscribed = (subscriptionId: string) => {
        void fetch({
            "X-Notify-Subscription": subscriptionId,
            body: {
                fetch: "channel",
                channel_handle: channelHandle,
            },
        });
    };

    const handleDataEvent = (event: FetchChannelEvent) => {
        dispatch(appendChannels(event.items));
        dispatch(invalidateFetchLimits());
        if (event.items.length > 0) {
            void navigate(routes.channels.details(event.items[0].handle));
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "channel",
                objectId: channelHandle,
                status: event.status,
            }),
        );
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
