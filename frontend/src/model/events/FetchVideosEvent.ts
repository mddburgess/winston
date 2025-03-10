import {VideoWithChannelIdDto} from "../VideoDto";

export type FetchVideosEvent = {
    channelId: string;
    status: 'FETCHING' | 'COMPLETED';
    videos: VideoWithChannelIdDto[];
}
