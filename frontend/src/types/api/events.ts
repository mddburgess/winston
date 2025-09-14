import type { Channel, Comment, Problem, PullOperationsRead, Video } from "#/api";
import type { ProblemDetail, TopLevelComment } from "#/types";

type FetchStatusEvent = {
  operation?: {
    operationType: "CHANNELS" | "VIDEOS" | "COMMENTS" | "REPLIES";
    objectId: string;
    status: "READY" | "FETCHING" | "SUCCESSFUL" | "FAILED";
  };
  status?: "COMPLETED" | "FAILED";
  error?: ProblemDetail;
};

type AppEvent = {
  event_id: string;
  event_type: string;
  operation?: PullOperationsRead[number];
  object_id: string;
  channel?: Channel;
  videos?: Video[];
  comments?: TopLevelComment[];
  replies?: Comment[];
  error?: Problem;
};

export type { AppEvent, FetchStatusEvent };
