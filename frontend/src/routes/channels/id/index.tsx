import {Link, useParams} from "react-router";
import {useListChannelsQuery, useListVideosByChannelIdQuery} from "../../../store/slices/api";
import {useMemo} from "react";
import {Breadcrumb, BreadcrumbItem, Container} from "react-bootstrap";
import {ChannelDetails} from "./ChannelDetails";
import {VideoCards} from "./VideoCards";

export const ChannelsIdRoute = () => {
    const { channelId } = useParams()

    const {data: channels} = useListChannelsQuery()
    const channel = useMemo(() => {
        return channels?.find(channel => channel.id === channelId)
    }, [channels])

    const {data: videos} = useListVideosByChannelIdQuery(channelId!)

    return (
        <Container>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{to: "/"}}>
                    Channels
                </BreadcrumbItem>
                <BreadcrumbItem active={true}>{channel?.title}</BreadcrumbItem>
            </Breadcrumb>
            {channel && <ChannelDetails channel={channel}/>}
            <VideoCards videos={videos}/>
        </Container>
    )
}
