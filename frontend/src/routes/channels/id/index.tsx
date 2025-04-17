import {Link, useParams, useSearchParams} from "react-router";
import {useFindChannelByIdQuery} from "../../../store/slices/channels";
import {useListVideosByChannelIdQuery, videosAdapter} from "../../../store/slices/videos";
import {useEffect, useMemo, useState} from "react";
import {Breadcrumb, BreadcrumbItem} from "react-bootstrap";
import {ChannelDetails} from "./ChannelDetails";
import {VideoCards} from "./VideoCards";
import {FetchVideosAlert} from "./FetchVideosAlert";
import {useAppDispatch} from "../../../store/hooks";
import {initFetchStateForChannel} from "../../../store/slices/fetches";
import {PaginationRow} from "../../../components/PaginationRow";

export const ChannelsIdRoute = () => {

    const {channelId} = useParams()
    const [searchParams, setSearchParams] = useSearchParams()

    const dispatch = useAppDispatch()

    const pageSize = 24;

    const [search, setSearch] = useState("");

    const {data: channel} = useFindChannelByIdQuery(channelId!)
    useEffect(() => {
        channel && dispatch(initFetchStateForChannel(channel))
    }, [channel])

    const {data: videos, isSuccess} = useListVideosByChannelIdQuery(channelId!)
    const videoList = isSuccess ? videosAdapter.getSelectors().selectAll(videos) : [];

    const displayedVideos = useMemo(
        () => {
            const page = parseInt(searchParams.get("p") ?? "1")
            return videoList.filter(video => video.title.toLowerCase().includes(search.toLowerCase()))
                .slice(pageSize * (page - 1), pageSize * page)
        },
        [videoList, pageSize, searchParams, search]
    );

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
            <PaginationRow
                name={"video"}
                total={videoList.length}
                pageSize={pageSize}
                page={parseInt(searchParams.get("p") ?? "1")}
                setPage={(page) => setSearchParams({ p: `${page}` })}
                search={search}
                setSearch={setSearch}
            />
            <VideoCards videos={displayedVideos}/>
            <PaginationRow
                name={"video"}
                total={videoList.length}
                pageSize={pageSize}
                page={parseInt(searchParams.get("p") ?? "1")}
                setPage={(page) => setSearchParams({ p: `${page}` })}
            />
        </>
    )
}
