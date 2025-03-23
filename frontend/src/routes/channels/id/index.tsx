import {Link, useParams} from "react-router";
import {useListChannelsQuery, useListVideosByChannelIdQuery} from "../../../store/slices/api";
import {useEffect, useMemo} from "react";
import {Breadcrumb, BreadcrumbItem, Container} from "react-bootstrap";
import {ChannelDetails} from "./ChannelDetails";
import {VideoCards} from "./VideoCards";
import {FetchVideosAlert} from "./FetchVideosAlert";
import {useAppDispatch, useAppSelector} from "../../../store/hooks";
import {DateTime} from "luxon";
import {initFetchStateForChannel} from "../../../store/slices/fetches";

export const ChannelsIdRoute = () => {
    const {channelId} = useParams()
    const dispatch = useAppDispatch()

    const {data: channels} = useListChannelsQuery()
    const channel = useMemo(() => {
        return channels?.find(channel => channel.id === channelId)
    }, [channels])
    useEffect(() => {
        channel && dispatch(initFetchStateForChannel(channel))
    }, [channel])

    const {data: videos} = useListVideosByChannelIdQuery(channelId!)
    const fetchedVideos = useAppSelector(state => state.fetches.videos[channelId!]?.data)
    const combinedVideos = useMemo(() => (fetchedVideos ?? []).concat(videos ?? [])
        .sort((a, b) => DateTime.fromISO(b.publishedAt).valueOf() - DateTime.fromISO(a.publishedAt).valueOf()),
    [videos, fetchedVideos])

    return (
        <>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{to: "/"}}>
                    Channels
                </BreadcrumbItem>
                {channel && <BreadcrumbItem active={true}>{channel.title}</BreadcrumbItem>}
            </Breadcrumb>
            {channel && <ChannelDetails channel={channel}/>}
            {channel && <FetchVideosAlert channel={channel}/>}
            <VideoCards videos={combinedVideos}/>
        </>
    )
}
