import {useFetchCommentsByVideoIdMutation} from "../../../store/slices/api";
import {useAppDispatch} from "../../../store/hooks";
import {EventSourceProvider} from "react-sse-hooks";
import {NotificationsSource} from "../../../components/NotificationsSource";
import {FetchCommentsEvent} from "../../../model/events/FetchCommentsEvent";
import {fetchedComments} from "../../../store/slices/fetches";

type FetchVideosActionProps = {
    videoId: string,
}

export const FetchCommentsAction = ({videoId}: FetchVideosActionProps) => {

    const [fetchCommentsByVideoId] = useFetchCommentsByVideoIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        fetchCommentsByVideoId({subscriptionId, videoId});
    }

    const handleEvent = (event: FetchCommentsEvent) => {
        dispatch(fetchedComments(event));
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                eventName={"fetch-comments"}
                onEvent={handleEvent}
            />
        </EventSourceProvider>
    )
}
