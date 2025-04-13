import {useFetchChannelByHandleMutation} from "../../store/slices/api";
import {NotificationsSource} from "../../components/NotificationsSource";
import {useNavigate} from "react-router";
import {EventSourceProvider} from "react-sse-hooks";
import {FetchChannelEvent} from "../../model/events/FetchEvent";
import {useAppDispatch} from "../../store/hooks";
import {appendFetchedChannels} from "../../store/slices/channels";

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

    const handleEvent = (event: FetchChannelEvent) => {
        if (event.status === 'COMPLETED') {
            dispatch(appendFetchedChannels(event.items));
            navigate(`/channels/${event.items[0].id}`);
        }
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                eventName={"fetch-channels"}
                onEvent={handleEvent}
            />
        </EventSourceProvider>
    );
}
