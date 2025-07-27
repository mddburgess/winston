import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/api";
import { appendComments, appendReplies } from "#/store/slices/comments";
import { updateFetchStatus } from "#/store/slices/fetches";
import { markVideoCommentsDisabled } from "#/store/slices/videos";
import type { Comment, Video } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";
import type { FetchStatusEvent } from "#/types";

type PullDataEvent = {
    objectId: string;
    comments?: TopLevelComment[];
    replies?: Comment[];
};

type Props = {
    video: Video;
};

const PullCommentsAndRepliesAction = ({ video }: Props) => {
    const [pull] = usePullMutation();
    const dispatch = useAppDispatch();

    const handleSubscribed = (eventListenerId: string) => {
        void pull({
            body: {
                event_listener_id: eventListenerId,
                operations: [
                    {
                        pull: "comments",
                        video_id: video.id,
                    },
                    {
                        pull: "replies",
                        video_id: video.id,
                    },
                ],
            },
        });
    };

    const handleDataEvent = (event: PullDataEvent) => {
        if (event.comments !== undefined) {
            dispatch(appendComments(event.objectId, event.comments));
        } else if (event.replies !== undefined) {
            dispatch(appendReplies(video.id, event.objectId, event.replies));
        }
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        dispatch(
            updateFetchStatus({
                fetchType: "comments",
                objectId: video.id,
                status: event.status,
            }),
        );
        if (event.status === "FAILED") {
            if (event.error.type === "/api/problem/comments-disabled") {
                dispatch(markVideoCommentsDisabled(video.id));
            }
        }
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

export { PullCommentsAndRepliesAction };
