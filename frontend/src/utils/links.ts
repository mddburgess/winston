import type { Author } from "#/types";

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
        notifications: {
            get: () => `/api/v1/notifications`,
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
        details: (authorHandle?: string) =>
            `/authors/${authorHandle ?? `:authorHandle`}`,
    },
    channels: {
        list: `/`,
        details: (handle?: string) => `/channels/${handle ?? `:handle`}`,
    },
    videos: {
        details: (videoId?: string) => `/videos/${videoId ?? `:videoId`}`,
    },
};

export { api, routes };
