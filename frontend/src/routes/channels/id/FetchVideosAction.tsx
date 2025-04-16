import {fetchedVideos, updateFetchStatus} from "../../../store/slices/fetches";
import {useAppDispatch} from "../../../store/hooks";
import {invalidateFetchLimits, useFetchVideosByChannelIdMutation} from "../../../store/slices/api";
import {videosAdapter, videosApiUtils} from "../../../store/slices/videos";
import {NotificationsSource} from "../../../components/NotificationsSource";
import {EventSourceProvider} from "react-sse-hooks";
import {FetchStatusEvent, FetchVideosEvent} from "../../../model/events/FetchEvent";

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

    const handleDataEvent = (event: FetchVideosEvent) => {
        dispatch(videosApiUtils.updateQueryData('listVideosByChannelId', channelId, (draft) => {
            videosAdapter.setMany(draft, event.items)
        }))
        dispatch(fetchedVideos(event));
    }

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(updateFetchStatus({
            fetchType: "videos",
            objectId: channelId,
            status: event.status,
        }))
        dispatch(invalidateFetchLimits());
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
            />
        </EventSourceProvider>
    )
}
