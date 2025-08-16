import type { Author, Channel, Video } from "#/api";

type AuthorListProps = {
    authors: Author[];
};

type AuthorProps = {
    author: Author;
};

type ChannelListProps = {
    channels: Channel[];
};

type ChannelProps = {
    channel: Channel;
};

type VideoListProps = {
    videos: Video[];
};

type VideoProps = {
    video: Video;
};

export type {
    AuthorListProps,
    AuthorProps,
    ChannelListProps,
    ChannelProps,
    VideoListProps,
    VideoProps,
};
