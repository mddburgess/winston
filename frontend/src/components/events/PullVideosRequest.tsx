import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateVideos } from "#/store/slices/backend";
import { invalidateFetchLimits } from "#/store/slices/limits";
import { pullVideosActive, pullVideosError, pullVideosResponse } from "#/store/slices/pullVideos";
import type { Video } from "#/api";
import type { PullOperationEvent, PullResultsEvent } from "#/components/events/types";

const PullVideosRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested } = useAppSelector((state) => state.pullVideos);

  const [pull] = usePullMutation();

  const requestPullVideos = (eventSubscriptionId: string) => {
    if (requested) {
      void pull({
        body: {
          event_subscription_id: eventSubscriptionId,
          operations: [{ pull: "videos", channel_handle: requested.channelHandle, range: requested.range }],
        },
      });
    }
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    if (event.operation.pull === "videos") {
      const channelHandle = event.operation.channel_handle;
      const status = event.operation.status;
      dispatch(pullVideosResponse({ channelHandle, status, count: 0 }));
      if (event.error) {
        dispatch(pullVideosError({ channelHandle, error: event.error }));
      }
      if (status !== "ready" && status !== "fetching") {
        dispatch(invalidateVideos());
      }
    }
  };

  const handlePullResultsEvent = (event: PullResultsEvent<Video>) => {
    if (event.operation.pull === "videos") {
      dispatch(invalidateFetchLimits());
      dispatch(
        pullVideosResponse({
          channelHandle: event.operation.channel_handle,
          status: event.operation.status,
          count: event.results.count,
        }),
      );
    }
  };

  const handleUnsubscribed = () => {
    dispatch(pullVideosActive(false));
  };

  return (
    active && (
      <PullEventsSource
        whenSubscribed={requestPullVideos}
        onPullOperationEvent={handlePullOperationEvent}
        onPullResultsEvent={handlePullResultsEvent}
        whenUnsubscribed={handleUnsubscribed}
      ></PullEventsSource>
    )
  );
};

export { PullVideosRequest };
