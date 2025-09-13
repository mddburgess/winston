import { map } from "lodash";
import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateComments, invalidateVideos } from "#/store/slices/backend";
import { invalidateFetchLimits } from "#/store/slices/limits";
import { pullCommentsActive, pullCommentsError, pullCommentsResponse } from "#/store/slices/pullComments";
import type { PullOperationEvent, PullResultsEvent } from "#/components/events/types";
import type { PullOperation, TopLevelComment } from "#/types";

const PullCommentsRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested } = useAppSelector((state) => state.pullComments);

  const [pull] = usePullMutation();

  const requestPullComments = (eventSubscriptionId: string) => {
    const operations: PullOperation[] = requested.flatMap((videoId) => [
      { pull: "comments", video_id: videoId },
      { pull: "replies", video_id: videoId },
    ]);
    void pull({ body: { event_subscription_id: eventSubscriptionId, operations } });
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    if (event.operation.pull === "comments") {
      dispatch(pullCommentsResponse({ videoId: event.operation.video_id, commentStatus: event.operation.status }));
      if (event.error) {
        dispatch(pullCommentsError({ videoId: event.operation.video_id, error: event.error }));
      }
    } else if (event.operation.pull === "replies") {
      dispatch(pullCommentsResponse({ videoId: event.operation.video_id ?? "", replyStatus: event.operation.status }));
      if (event.error) {
        dispatch(pullCommentsError({ videoId: event.operation.video_id ?? "", error: event.error }));
      }
    }
  };

  const handlePullResultsEvent = (event: PullResultsEvent<TopLevelComment>) => {
    if (event.operation.pull === "comments") {
      const replies = event.results.items.flatMap((comment) => comment.replies ?? []);
      dispatch(invalidateFetchLimits());
      dispatch(
        pullCommentsResponse({
          videoId: event.operation.video_id,
          commentStatus: event.operation.status,
          commentCount: event.results.count,
          replyIds: map(replies, "id"),
        }),
      );
    } else if (event.operation.pull === "replies") {
      dispatch(invalidateFetchLimits());
      dispatch(
        pullCommentsResponse({
          videoId: event.operation.video_id ?? "",
          replyStatus: event.operation.status,
          replyIds: map(event.results.items, "id"),
        }),
      );
    }
  };

  const handleUnsubscribed = () => {
    dispatch(pullCommentsActive(false));
    dispatch(invalidateVideos());
    dispatch(invalidateComments());
  };

  return (
    active && (
      <PullEventsSource
        whenSubscribed={requestPullComments}
        onPullOperationEvent={handlePullOperationEvent}
        onPullResultsEvent={handlePullResultsEvent}
        whenUnsubscribed={handleUnsubscribed}
      />
    )
  );
};

export { PullCommentsRequest };
