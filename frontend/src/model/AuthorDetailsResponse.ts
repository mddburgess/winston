import {VideoWithChannelIdDto} from "./VideoDto";
import {AuthorDto} from "./AuthorDto";
import {CommentDto} from "./CommentDto";

export type AuthorDetailsResponse = {
    author: AuthorDto;
    comments: CommentDto[];
    videos: VideoWithChannelIdDto[];
}
