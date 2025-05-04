import type { Author, Channel, Video } from "../types";

export const api = {
    authors: {
        get: () => `/v1/authors`,
        handle: {
            get: (handle: string) => `/v1/authors/${handle}`,
        },
    },
    fetch: {
        post: () => `/v1/fetch`,
        limits: {
            get: () => `/v1/fetch/limits`,
        },
    },
    channels: {
        get: () => `/v1/channels`,
        handle: {
            get: (handle: string) => `/v1/channels/${handle}`,
            videos: {
                get: (handle: string) => `/v1/channels/${handle}/videos`,
            },
        },
    },
    notifications: {
        get: () => `/api/v1/notifications`,
    },
    videos: {
        id: {
            get: (videoId: string) => `/v1/videos/${videoId}`,
            comments: {
                get: (videoId: string) => `/v1/videos/${videoId}/comments`,
            },
        },
    },
};

export const routes = {
    home: `/`,
    authors: {
        list: `/authors`,
        details: (author?: Author) =>
            `/authors/${author?.displayName ?? `:authorHandle`}`,
    },
    channels: {
        list: `/`,
        details: (channel?: Channel) =>
            `/channels/${channel?.customUrl ?? `:channelHandle`}`,
    },
    videos: {
        details: (video?: Video) => `/videos/${video?.id ?? `:videoId`}`,
    },
};
