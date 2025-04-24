import { useAppDispatch } from "../../../store/hooks";
import {
    invalidateFetchLimits,
    useFetchRepliesByVideoIdMutation,
} from "../../../store/slices/api";
import {
    FetchCommentsEvent,
    FetchStatusEvent,
} from "../../../model/events/FetchEvent";
import { EventSourceProvider } from "react-sse-hooks";
import { NotificationsSource } from "../../../components/NotificationsSource";
import { updateFetchStatus } from "../../../store/slices/fetches";
import {
    commentsAdapter,
    commentsApiUtils,
    repliesAdapter,
} from "../../../store/slices/comments";

type FetchRepliesActionProps = {
    videoId: string;
};

export const FetchVideoRepliesAction = ({
    videoId,
}: FetchRepliesActionProps) => {
    const [fetchRepliesByVideoId] = useFetchRepliesByVideoIdMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (subscriptionId: string) => {
        void fetchRepliesByVideoId({ subscriptionId, videoId });
    };

    const handleDataEvent = (event: FetchCommentsEvent) => {
        if (event.items.length > 0) {
            const commentId = event.objectId;
            dispatch(
                commentsApiUtils.updateQueryData(
                    "listCommentsByVideoId",
                    videoId,
                    (draft) => {
                        const comment = commentsAdapter
                            .getSelectors()
                            .selectById(draft, commentId);
                        commentsAdapter.setOne(draft, {
                            ...comment,
                            replies: repliesAdapter.addMany(
                                comment.replies,
                                event.items,
                            ),
                        });
                    },
                ),
            );
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "replies",
                objectId: videoId,
                status: event.status,
            }),
        );
        dispatch(invalidateFetchLimits());
    };

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
            />
        </EventSourceProvider>
    );
};
