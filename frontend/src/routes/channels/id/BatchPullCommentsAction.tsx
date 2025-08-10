import { useState } from "react";
import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { NotificationsSource } from "#/components/NotificationsSource";
import { useAppDispatch } from "#/store/hooks";
import {
    batchPullCommentsData,
    batchPullCommentsStatus,
} from "#/store/slices/pulls";
import type { Channel, Comment, PullOperations } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";
import type { PullCommentsState } from "#/store/slices/pulls";
import type { FetchStatusEvent } from "#/types";

type PullDataEvent = {
    objectId: string;
    comments?: TopLevelComment[];
    replies?: Comment[];
};

type Props = {
    channel: Channel;
    pullComments: PullCommentsState[];
};

const BatchPullCommentsAction = ({ channel, pullComments }: Props) => {
    const [pull] = usePullMutation();
    const dispatch = useAppDispatch();
    const [videoId, setVideoId] = useState("");

    const handleSubscribed = (eventListenerId: string) => {
        const operations: PullOperations = pullComments.flatMap(
            (pullComment) => [
                { pull: "comments", video_id: pullComment.video.id },
                { pull: "replies", video_id: pullComment.video.id },
            ],
        );
        void pull({ body: { event_listener_id: eventListenerId, operations } });
    };

    const handleDataEvent = (event: PullDataEvent) => {
        dispatch(
            batchPullCommentsData({
                channelId: channel.id,
                videoId: videoId,
                comments: event.comments,
                replies: event.replies,
            }),
        );
    };

    const handleStatusEvent = (event: FetchStatusEvent) => {
        if (event.operation) {
            if (
                event.operation.operationType !== "COMMENTS" ||
                event.operation.status !== "SUCCESSFUL"
            ) {
                setVideoId(event.operation.objectId);
                dispatch(
                    batchPullCommentsStatus({
                        channelId: channel.id,
                        videoId: event.operation.objectId,
                        status: event.operation.status,
                    }),
                );
            }
        }
    };

    return (
        <EventSourceProvider>
            <NotificationsSource
                onSubscribed={handleSubscribed}
                onDataEvent={handleDataEvent}
                onStatusEvent={handleStatusEvent}
                hideSpinner={true}
            />
        </EventSourceProvider>
    );
};

const buildOperations = (videoIds: string[]) => {
    return;
};

export { BatchPullCommentsAction };
