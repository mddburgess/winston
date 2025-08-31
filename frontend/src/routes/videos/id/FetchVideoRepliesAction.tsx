import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { appendReplies } from "#/store/slices/comments";
import { updateFetchStatus } from "#/store/slices/fetches";
import { invalidateFetchLimits } from "#/store/slices/limits";
import type { AppEvent, FetchStatusEvent, IdProps } from "#/types";

export const FetchVideoRepliesAction = ({ id: videoId }: IdProps) => {
  const dispatch = useAppDispatch();
  const [pull] = usePullMutation();

  const handleSubscribed = (eventListenerId: string) => {
    void pull({
      body: {
        event_subscription_id: eventListenerId,
        operations: [{ pull: "replies", video_id: videoId }],
      },
    });
  };

  const handleDataEvent = (event: AppEvent) => {
    if (event.replies && event.replies.length > 0) {
      const commentId = event.object_id;
      dispatch(appendReplies(videoId, commentId, event.replies));
    }
  };

  const handleStatusEvent = (event: FetchStatusEvent) => {
    if (event.status) {
      dispatch(
        updateFetchStatus({
          fetchType: "replies",
          objectId: videoId,
          status: event.status,
        }),
      );
      dispatch(invalidateFetchLimits());
    }
  };

  return <PullEventsSource whenSubscribed={handleSubscribed} />;
};
