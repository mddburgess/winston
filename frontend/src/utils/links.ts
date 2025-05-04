import { AuthorDto } from "../model/authors/AuthorDto";
import { ChannelDto } from "../model/ChannelDto";
import { VideoWithChannelIdDto } from "../model/VideoDto";

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
