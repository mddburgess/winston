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

export { routes };
