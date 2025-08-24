import type {
    Author,
    ListCommentsResponse,
    PullOperationRead,
    Video,
} from "#/api";

type ChannelSummary = Video["channel"];

type PullOperationStatus = NonNullable<PullOperationRead["status"]>;

type TopLevelComment = ListCommentsResponse["comments"][number];

type VideoStatistics = NonNullable<Author["video_statistics"]>[number];

export type {
    ChannelSummary,
    PullOperationStatus,
    TopLevelComment,
    VideoStatistics,
};
