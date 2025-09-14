import type { Problem } from "#/api";
import type { Dictionary } from "lodash";

const apiErrors: Dictionary<Problem> = {
  authorNotFound: {
    type: "/api/problem/author-not-found",
    title: "Not Found",
    status: 404,
    detail: "The requested author was not found.",
  },
  channelNotFound: {
    type: "/api/problem/channel-not-found",
    title: "Not Found",
    status: 404,
    detail: "The requested channel was not found.",
  },
  channelNotPulled: {
    type: "/api/problem/channel-not-pulled",
    title: "Unprocessable Entity",
    status: 422,
    detail: "The specified channel must be pulled before videos for that channel may be pulled.",
  },
  commentNotFound: {
    type: "/api/problem/comment-not-found",
    title: "Not Found",
    status: 404,
    detail: "The requested comment was not found.",
  },
  commentsDisabled: {
    type: "/api/problem/comments-disabled",
    title: "Unprocessable Entity",
    status: 422,
    detail: "Commends are disabled for the requested video.",
  },
  quotaExceeded: {
    type: "/api/problem/quota-exceeded",
    title: "Too Many Requests",
    status: 429,
    detail: "The YouTube API request quota has been exceeded.",
  },
  serviceShutdown: {
    type: "/api/problem/service-shutdown",
    title: "Service Unavailable",
    status: 503,
    detail: "The service is shutting down and is no longer available.",
  },
  subscriptionClosed: {
    type: "/api/problem/subscription-closed",
    title: "Bad Request",
    status: 400,
    detail: "The specified event subscription is not open.",
  },
  videoNotFound: {
    type: "/api/problem/video-not-found",
    title: "Not Found",
    status: 404,
    detail: "The requested video was not found.",
  },
};

export { apiErrors };
