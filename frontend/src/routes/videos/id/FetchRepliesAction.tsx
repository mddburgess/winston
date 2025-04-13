import { useAppDispatch } from "../../../store/hooks";
import { useFetchRepliesByCommentIdMutation} from "../../../store/slices/api";
import {FetchCommentsEvent, FetchStatusEvent} from "../../../model/events/FetchEvent";
import {EventSourceProvider} from "react-sse-hooks";
import {NotificationsSource} from "../../../components/NotificationsSource";
import {fetchedReplies} from "../../../store/slices/fetches";

type FetchRepliesActionProps = {
    commentId: string;
}

export const FetchRepliesAction = ({commentId}: FetchRepliesActionProps) => {

    const [fetchRepliesByCommentId] = useFetchRepliesByCommentIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        fetchRepliesByCommentId({subscriptionId, commentId});
    }

    const handleDataEvent = (event: FetchCommentsEvent) => {
        dispatch(fetchedReplies(event));
    }

    const handleStatusEvent = (statusEvent: FetchStatusEvent) => {
        // no-op
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
