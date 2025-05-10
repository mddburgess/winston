import type { VideoWithChannelIdDto } from "../VideoDto";
import type { AuthorDto } from "./AuthorDto";
import type { CommentDto } from "../CommentDto";

export type AuthorDetailsResponse = {
    author: AuthorDto;
    comments: CommentDto[];
    videos: VideoWithChannelIdDto[];
};
