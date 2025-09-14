import type { Author, Channel, Comment, Video } from "#/api";

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

type CommentProps = {
  comment: Comment;
};

type IdProps = {
  id: string;
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
  CommentProps,
  IdProps,
  VideoListProps,
  VideoProps,
};
