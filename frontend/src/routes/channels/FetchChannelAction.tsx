import {useFetchChannelByHandleMutation} from "../../store/slices/api";
import {NotificationsSource} from "../../components/NotificationsSource";
import {useNavigate} from "react-router";
import {EventSourceProvider} from "react-sse-hooks";
import {FetchChannelEvent} from "../../model/events/FetchEvent";

type FetchChannelActionProps = {
    channelHandle: string
}

export const FetchChannelAction = ({channelHandle}: FetchChannelActionProps) => {

    const [fetchChannelsByHandle] = useFetchChannelByHandleMutation();
    const navigate = useNavigate()

    const handleSubscribed = (subscriptionId: string) => {
        fetchChannelsByHandle({ subscriptionId, channelHandle });
    }

    const handleEvent = (event: FetchChannelEvent) => {
        navigate(`/channels/${event.items[0].id}`);
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
