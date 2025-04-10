import {fetchedVideos} from "../../../store/slices/fetches";
import {useAppDispatch} from "../../../store/hooks";
import {useFetchVideosByChannelIdMutation} from "../../../store/slices/api";
import {NotificationsSource} from "../../../components/NotificationsSource";
import {EventSourceProvider} from "react-sse-hooks";
import {FetchVideosEvent} from "../../../model/events/FetchEvent";

type FetchVideosWidgetProps = {
    channelId: string,
    mode: 'ALL' | 'LATEST',
}

export const FetchVideosAction = ({channelId, mode}: FetchVideosWidgetProps) => {

    const [fetchVideosByChannelId] = useFetchVideosByChannelIdMutation()
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        fetchVideosByChannelId({ subscriptionId, channelId, mode });
    }

    const handleEvent = (event: FetchVideosEvent) => {
        dispatch(fetchedVideos(event));
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                eventName={"fetch-videos"}
                onEvent={handleEvent}
            />
        </EventSourceProvider>
    )
}
