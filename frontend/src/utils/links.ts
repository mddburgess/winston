const api = {
    v1: {
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
