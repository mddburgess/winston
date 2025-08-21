import type { Author, ListCommentsResponse, Video } from "#/api";

type ChannelSummary = Video["channel"];

type TopLevelComment = ListCommentsResponse["comments"][number];

type VideoStatistics = NonNullable<Author["video_statistics"]>[number];

export type { ChannelSummary, TopLevelComment, VideoStatistics };
