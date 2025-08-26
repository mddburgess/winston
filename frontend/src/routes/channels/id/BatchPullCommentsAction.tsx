import { useState } from "react";
import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { AppEventsSource } from "#/components/events/AppEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/limits";
import {
    updatePullCommentsData,
    updatePullCommentsStatus,
} from "#/store/slices/pullVideoComments";
import type { PullOperations } from "#/api";
import type { AppEvent, VideoListProps } from "#/types";

const BatchPullCommentsAction = ({ videos }: VideoListProps) => {
    const dispatch = useAppDispatch();
    const [pull] = usePullMutation();
    const [videoId, setVideoId] = useState("");

    const handleSubscribed = (eventListenerId: string) => {
        const operations: PullOperations = videos.flatMap((video) => [
            { pull: "comments", video_id: video.id },
            { pull: "replies", video_id: video.id },
        ]);
        void pull({ body: { event_listener_id: eventListenerId, operations } });
    };

    const handleOperationEvent = (event: AppEvent) => {
        if (!event.operation) {
            return;
        }
        if (event.operation.pull === "comments") {
            setVideoId(event.operation.video_id);
            dispatch(
                updatePullCommentsStatus({
                    videoId: event.operation.video_id,
                    commentsStatus: event.operation.status,
                }),
            );
        } else if (event.operation.pull === "replies") {
            setVideoId(event.operation.video_id!);
            dispatch(
                updatePullCommentsStatus({
                    videoId: event.operation.video_id!,
                    repliesStatus: event.operation.status,
                }),
            );
        }
        dispatch(invalidateFetchLimits());
    };

    const handleDataEvent = (event: AppEvent) => {
        dispatch(
            updatePullCommentsData({
                videoId: videoId,
                comments: event.comments,
                replies: event.replies,
            }),
        );
        dispatch(invalidateFetchLimits());
    };

    return (
        <EventSourceProvider>
            <AppEventsSource
                onSubscribed={handleSubscribed}
                onOperationEvent={handleOperationEvent}
                onDataEvent={handleDataEvent}
                hideSpinner={true}
            />
        </EventSourceProvider>
    );
};

export { BatchPullCommentsAction };
