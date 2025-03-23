import {useFetchChannelByHandleMutation} from "../../store/slices/api";
import {NotificationsSource} from "../../components/NotificationsSource";
import {FetchChannelEvent} from "../../model/events/FetchChannelEvent";
import {useNavigate} from "react-router";
import {EventSourceProvider} from "react-sse-hooks";

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
        navigate(`/channels/${event.channel.id}`);
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                eventName={"fetch-channel"}
                onEvent={handleEvent}
            />
        </EventSourceProvider>
    );
}
