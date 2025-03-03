import {useParams} from "react-router";
import {useListChannelsQuery, useListVideosByChannelIdQuery} from "../../../store/slices/api";
import {useMemo} from "react";
import {Container} from "react-bootstrap";
import {ChannelDetails} from "./ChannelDetails";
import {VideoList} from "./VideoList";

export const ChannelsIdRoute = () => {
    const { channelId } = useParams()

    const {data: channels} = useListChannelsQuery()
    const channel = useMemo(() => {
        return channels?.find(channel => channel.id === channelId)
    }, [channels])

    const {data: videos} = useListVideosByChannelIdQuery(channelId!)

    return (
        <Container>
            <h1>Videos</h1>
            {channel && <ChannelDetails channel={channel}/>}
            <VideoList videos={videos}/>
        </Container>
    )
}
