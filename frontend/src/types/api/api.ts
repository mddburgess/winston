import type { Author, ListCommentsResponse, PullOperationsRead, PullRequest, Video } from "#/api";

type ChannelSummary = Video["channel"];

type PullOperationRead = PullOperationsRead[number];

type PullOperationStatus = NonNullable<PullOperationRead["status"]>;

type PullRequestRead = PullRequest["body"] & {
  status: PullRequestStatus;
  id: string;
};

type PullRequestStatus = "accepted" | "fetching" | "paused" | "completed";

type TopLevelComment = ListCommentsResponse["comments"][number];

type VideoStatistics = NonNullable<Author["video_statistics"]>[number];

export type {
  ChannelSummary,
  PullOperationRead,
  PullOperationStatus,
  PullRequestRead,
  PullRequestStatus,
  TopLevelComment,
  VideoStatistics,
};
