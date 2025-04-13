import {useFetchCommentsByVideoIdMutation} from "../../../store/slices/api";
import {commentsAdapter, commentsApiUtils} from "../../../store/slices/comments";
import {useAppDispatch} from "../../../store/hooks";
import {EventSourceProvider} from "react-sse-hooks";
import {NotificationsSource} from "../../../components/NotificationsSource";
import {fetchedComments, updateFetchStatus} from "../../../store/slices/fetches";
import {FetchCommentsEvent, FetchStatusEvent} from "../../../model/events/FetchEvent";
import {videosApiUtils} from "../../../store/slices/videos";

type FetchVideosActionProps = {
    videoId: string,
}

export const FetchCommentsAction = ({videoId}: FetchVideosActionProps) => {

    const [fetchCommentsByVideoId] = useFetchCommentsByVideoIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        fetchCommentsByVideoId({subscriptionId, videoId});
    }

    const handleDataEvent = (event: FetchCommentsEvent) => {
        dispatch(commentsApiUtils.updateQueryData("listCommentsByVideoId", videoId, draft => {
            commentsAdapter.addMany(draft, event.items);
        }))
        dispatch(fetchedComments(event));
    }

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(updateFetchStatus({
            fetchType: "comments",
            objectId: videoId,
            status: event.status,
        }))
        if (event.status === "FAILED") {
            if (event.error.type === "/api/problem/comments-disabled") {
                dispatch(videosApiUtils.updateQueryData("findVideoById", videoId, draft => {
                    draft.commentsDisabled = true
                }))
            }
        }
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
