import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateFetchLimits } from "#/store/slices/limits";
import { pullRepliesActive, pullRepliesError, pullRepliesResponse } from "#/store/slices/pullReplies";
import type { Comment } from "#/api";
import type { PullOperationEvent, PullResultsEvent } from "#/components/events/types";
import { invalidateComments } from "#/store/slices/backend.ts";

const PullRepliesRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested } = useAppSelector((state) => state.pullReplies);

  const [pull] = usePullMutation();

  const whenSubscribed = (eventSubscriptionId: string) => {
    if (requested) {
      void pull({
        body: {
          event_subscription_id: eventSubscriptionId,
          operations: [{ pull: "replies", comment_id: requested }],
        },
      });
    }
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    if (event.operation.pull === "replies") {
      const commentId = event.operation.comment_id ?? "";
      dispatch(pullRepliesResponse({ commentId, status: event.operation.status, count: 0 }));
      if (event.error) {
        dispatch(pullRepliesError({ commentId, error: event.error }));
      }
    }
  };

  const handlePullResultsEvent = (event: PullResultsEvent<Comment>) => {
    if (event.operation.pull === "replies") {
      dispatch(invalidateFetchLimits());
      dispatch(
        pullRepliesResponse({
          commentId: event.operation.comment_id ?? "",
          status: event.operation.status,
          count: event.results.count,
        }),
      );
    }
  };

  const whenUnsubscribed = () => {
    dispatch(pullRepliesActive(false));
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
