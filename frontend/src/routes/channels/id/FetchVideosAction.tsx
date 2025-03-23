import {fetchedVideos} from "../../../store/slices/fetches";
import {useAppDispatch} from "../../../store/hooks";
import {useFetchVideosByChannelIdMutation} from "../../../store/slices/api";
import {FetchVideosEvent} from "../../../model/events/FetchVideosEvent";
import {NotificationsSource} from "../../../components/NotificationsSource";

type FetchVideosWidgetProps = {
    channelId: string
}

export const FetchVideosAction = ({channelId}: FetchVideosWidgetProps) => {

    const [fetchVideosByChannelId] = useFetchVideosByChannelIdMutation()
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        fetchVideosByChannelId({ subscriptionId, channelId });
    }

    const handleEvent = (event: FetchVideosEvent) => {
        dispatch(fetchedVideos(event));
    }

    return (
        <NotificationsSource
            onSubscribed={handleSubscribed}
            eventName={"fetch-videos"}
            onEvent={handleEvent}
        />
    )
}
