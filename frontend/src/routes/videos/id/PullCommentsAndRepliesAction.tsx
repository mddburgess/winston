import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { appendComments, appendReplies } from "#/store/slices/comments";
import { updateFetchStatus } from "#/store/slices/fetches";
import { invalidateFetchLimits } from "#/store/slices/limits";
import { markVideoCommentsDisabled } from "#/store/slices/videos";
import type { AppEvent, FetchStatusEvent, VideoProps } from "#/types";

const PullCommentsAndRepliesAction = ({ video }: VideoProps) => {
  const dispatch = useAppDispatch();
  const [pull] = usePullMutation();

  const handleSubscribed = (eventListenerId: string) => {
    void pull({
      body: {
        event_subscription_id: eventListenerId,
        operations: [
          { pull: "comments", video_id: video.id },
          { pull: "replies", video_id: video.id },
        ],
      },
    });
  };

  const handleDataEvent = (event: AppEvent) => {
    if (event.comments !== undefined) {
      dispatch(appendComments(event.object_id, event.comments));
    } else if (event.replies !== undefined) {
      dispatch(appendReplies(video.id, event.object_id, event.replies));
    }
  };

  const handleStatusEvent = (event: FetchStatusEvent) => {
    if (event.status) {
      dispatch(
        updateFetchStatus({
          fetchType: "comments",
          objectId: video.id,
          status: event.status,
        }),
      );
      if (event.status === "FAILED") {
        if (event.error?.type === "/api/problem/comments-disabled") {
          dispatch(markVideoCommentsDisabled(video.id));
        }
      }
      dispatch(invalidateFetchLimits());
    }
  };

  return <PullEventsSource whenSubscribed={handleSubscribed} />;
};

export { PullCommentsAndRepliesAction };
