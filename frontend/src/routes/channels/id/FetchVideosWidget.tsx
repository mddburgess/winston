import {useEventSource, useEventSourceListener} from "react-sse-hooks";
import {fetchedVideos} from "../../../store/slices/fetches";
import {Spinner} from "react-bootstrap";
import {useAppDispatch} from "../../../store/hooks";
import {SubscriptionEvent} from "../../../model/events/SubscriptionEvent";
import {subscribedToNotifications, unsubscribedFromNotifications} from "../../../store/slices/notifications";
import {useEffect} from "react";
import {useFetchVideosByChannelIdMutation} from "../../../store/slices/api";
import {FetchVideosEvent} from "../../../model/events/FetchVideosEvent";

type FetchVideosWidgetProps = {
    channelId: string
}

export const FetchVideosWidget = ({channelId}: FetchVideosWidgetProps) => {

    const dispatch = useAppDispatch();
    const [fetchVideosByChannelId] = useFetchVideosByChannelIdMutation()

    const eventSource = useEventSource({
        source: `/api/notifications`,
    })

    useEffect(() => {
        eventSource.onerror = (event) => {
            eventSource.close()
            dispatch(unsubscribedFromNotifications())
        }
    }, [eventSource])

    useEventSourceListener<SubscriptionEvent>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "message",
            listener: (event) => {
                dispatch(subscribedToNotifications(event.data.subscriptionId))
                fetchVideosByChannelId({
                    channelId,
                    subscriptionId: event.data.subscriptionId,
                })
            },
            options: { once: true }
        }
    }, [eventSource, channelId, fetchVideosByChannelId])

    useEventSourceListener<FetchVideosEvent>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "fetch-videos",
            listener: event => {
                dispatch(fetchedVideos(event.data))
            }
        }
    }, [eventSource])

    return (
        <Spinner size={"sm"} className={"ms-2"}/>
    )
}
