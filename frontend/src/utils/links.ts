import { AuthorDto } from "../model/authors/AuthorDto";
import { ChannelDto } from "../model/ChannelDto";
import { VideoWithChannelIdDto } from "../model/VideoDto";

export const api = {
    authors: {
        get: () => `/authors`,
        handle: {
            get: (handle: string) => `/authors/${handle}`,
        },
    },
    fetch: {
        post: () => `/fetch`,
        limits: {
            get: () => `/fetch/limits`,
        },
    },
    channels: {
        get: () => `/channels`,
        handle: {
            get: (handle: string) => `/channels/${handle}`,
            videos: {
                get: (handle: string) => `/channels/${handle}/videos`,
            },
        },
    },
    notifications: {
        get: () => `/api/notifications`,
    },
    videos: {
        id: {
            get: (videoId: string) => `/videos/${videoId}`,
            comments: {
                get: (videoId: string) => `/videos/${videoId}/comments`,
            },
        },
    },
};

export const routes = {
    home: `/`,
    authors: {
        list: `/authors`,
        details: (author?: AuthorDto) =>
            `/authors/${author?.displayName ?? `:authorHandle`}`,
    },
    channels: {
        list: `/`,
        details: (channel?: ChannelDto) =>
            `/channels/${channel?.customUrl ?? `:channelHandle`}`,
    },
    videos: {
        details: (video?: VideoWithChannelIdDto) =>
            `/videos/${video?.id ?? `:videoId`}`,
    },
};
