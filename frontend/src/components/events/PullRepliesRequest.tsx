import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateComments, invalidateVideos } from "#/store/slices/backend";
import { invalidateFetchLimits } from "#/store/slices/limits";
import { pullRepliesActive, pullRepliesError, pullRepliesResponse } from "#/store/slices/pullReplies";
import type { Comment } from "#/api";
import type { PullOperationEvent, PullResultsEvent } from "#/components/events/types";

const PullRepliesRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested } = useAppSelector((state) => state.pullReplies);

  const [pull] = usePullMutation();

  const whenSubscribed = (eventSubscriptionId: string) => {
    void pull({
      body: {
        event_subscription_id: eventSubscriptionId,
        operations: [{ pull: "replies", video_id: requested.videoId, comment_id: requested.commentId }],
      },
    });
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    if (event.operation.pull === "replies") {
      const id = event.operation.comment_id ?? event.operation.video_id ?? "";
      dispatch(pullRepliesResponse({ id: id, status: event.operation.status, count: 0 }));
      if (event.error) {
        dispatch(pullRepliesError({ commentId: id, error: event.error }));
      }
    }
  };

  const handlePullResultsEvent = (event: PullResultsEvent<Comment>) => {
    if (event.operation.pull === "replies") {
      dispatch(invalidateFetchLimits());
      dispatch(
        pullRepliesResponse({
          id: event.operation.comment_id ?? event.operation.video_id ?? "",
          status: event.operation.status,
          count: event.results.count,
        }),
      );
    }
  };

  const whenUnsubscribed = () => {
    dispatch(pullRepliesActive(false));
    dispatch(invalidateVideos());
    dispatch(invalidateComments());
  };

  return (
    active && (
      <PullEventsSource
        whenSubscribed={whenSubscribed}
        onPullOperationEvent={handlePullOperationEvent}
        onPullResultsEvent={handlePullResultsEvent}
        whenUnsubscribed={whenUnsubscribed}
      />
    )
  );
};

export { PullRepliesRequest };
