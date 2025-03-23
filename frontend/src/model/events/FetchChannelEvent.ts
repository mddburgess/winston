import {ChannelDto} from "../ChannelDto";

export type FetchChannelEvent = {
    channelHandle: string;
    status: 'FETCHING' | 'COMPLETED';
    channel: ChannelDto;
}
