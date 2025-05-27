import type { Author, Video } from "#/types";

const api = {
    v1: {
        authors: {
            get: () => `/api/v1/authors`,
            handle: {
                get: (handle: string) => `/api/v1/authors/${handle}`,
            },
        },
        fetch: {
            post: () => `/api/v1/fetch`,
            limits: {
                get: () => `/api/v1/fetch/limits`,
            },
        },
        channels: {
            handle: {
                videos: {
                    get: (handle: string) =>
                        `/api/v1/channels/${handle}/videos`,
                },
            },
        },
        notifications: {
            get: () => `/api/v1/notifications`,
        },
        videos: {
            id: {
                get: (videoId: string) => `/api/v1/videos/${videoId}`,
                comments: {
                    get: (videoId: string) =>
                        `/api/v1/videos/${videoId}/comments`,
                },
            },
        },
    },
    v2: {
        authors: {
            handle: {
                get: (handle: string) => `/api/v2/authors/${handle}`,
            },
        },
    },
};

const routes = {
    home: `/`,
    authors: {
        list: `/authors`,
        details: (author?: Author) =>
            `/authors/${author?.displayName ?? `:authorHandle`}`,
    },
    channels: {
        list: `/`,
        details: (handle?: string) => `/channels/${handle ?? `:handle`}`,
    },
    videos: {
        details: (video?: Video) => `/videos/${video?.id ?? `:videoId`}`,
    },
};

export { api, routes };
