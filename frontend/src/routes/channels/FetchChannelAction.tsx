import { useNavigate } from "react-router";
import { EventSourceProvider } from "react-sse-hooks";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import {
    invalidateFetchLimits,
    useFetchChannelByHandleMutation,
} from "#/store/slices/api";
import { appendFetchedChannels } from "#/store/slices/channels";
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

    const [fetchChannelsByHandle] = useFetchChannelByHandleMutation();

    const handleSubscribed = (subscriptionId: string) => {
        void fetchChannelsByHandle({ subscriptionId, channelHandle });
    };

    const handleDataEvent = (event: FetchChannelEvent) => {
        dispatch(appendFetchedChannels(event.items));
        dispatch(invalidateFetchLimits());
        if (event.items.length > 0) {
            void navigate(routes.channels.details(event.items[0]));
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
