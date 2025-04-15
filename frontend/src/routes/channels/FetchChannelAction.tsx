import {useFetchChannelByHandleMutation} from "../../store/slices/api";
import {NotificationsSource} from "../../components/NotificationsSource";
import {useNavigate} from "react-router";
import {EventSourceProvider} from "react-sse-hooks";
import {FetchChannelEvent, FetchStatusEvent} from "../../model/events/FetchEvent";
import {useAppDispatch} from "../../store/hooks";
import {appendFetchedChannels} from "../../store/slices/channels";
import {updateFetchStatus} from "../../store/slices/fetches";
type FetchChannelActionProps = {
    channelHandle: string
}

export const FetchChannelAction = ({channelHandle}: FetchChannelActionProps) => {

    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [fetchChannelsByHandle] = useFetchChannelByHandleMutation();

    const handleSubscribed = (subscriptionId: string) => {
        fetchChannelsByHandle({ subscriptionId, channelHandle });
    }

    const handleDataEvent = (event: FetchChannelEvent) => {
        dispatch(appendFetchedChannels(event.items));
        if (event.items.length > 0) {
            navigate(`/channels/${event.items[0].id}`);
        }
    }

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(updateFetchStatus({
            fetchType: "channel",
            objectId: channelHandle,
            status: event.status
        }));
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
            />
        </EventSourceProvider>
    );
}
