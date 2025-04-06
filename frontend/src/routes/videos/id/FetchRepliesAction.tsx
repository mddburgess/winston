import { useAppDispatch } from "../../../store/hooks";
import { useFetchRepliesByCommentIdMutation} from "../../../store/slices/api";
import {FetchCommentsEvent} from "../../../model/events/FetchEvent";
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

    const handleEvent = (event: FetchCommentsEvent) => {
        dispatch(fetchedReplies(event));
    }

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                eventName={"fetch-replies"}
                onEvent={handleEvent}
            />
        </EventSourceProvider>
    )
}
