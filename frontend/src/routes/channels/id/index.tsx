import {Link, useParams} from "react-router";
import {useFindChannelByIdQuery, useListVideosByChannelIdQuery} from "../../../store/slices/api";
import {useEffect, useMemo, useState} from "react";
import {Breadcrumb, BreadcrumbItem, Col, Container, Pagination, Row} from "react-bootstrap";
import {ChannelDetails} from "./ChannelDetails";
import {VideoCards} from "./VideoCards";
import {FetchVideosAlert} from "./FetchVideosAlert";
import {useAppDispatch, useAppSelector} from "../../../store/hooks";
import {DateTime} from "luxon";
import {initFetchStateForChannel} from "../../../store/slices/fetches";
import {PaginationRow} from "../../../components/PaginationRow";
import {descBy} from "../../../utils";

export const ChannelsIdRoute = () => {

    const {channelId} = useParams()
    const dispatch = useAppDispatch()

    const pageSize = 24;
    const [page, setPage] = useState(1);

    const [search, setSearch] = useState("");

    const {data: channel} = useFindChannelByIdQuery(channelId!)
    useEffect(() => {
        channel && dispatch(initFetchStateForChannel(channel))
    }, [channel])

    const {data: videos} = useListVideosByChannelIdQuery(channelId!)
    const fetchedVideos = useAppSelector(state => state.fetches.videos[channelId!]?.data)
    const combinedVideos = useMemo(() => (fetchedVideos ?? []).concat(videos ?? [])
        .sort(descBy(video => DateTime.fromISO(video.publishedAt).valueOf())),
    [videos, fetchedVideos])

    const displayedVideos = useMemo(
        () => combinedVideos.filter(video => video.title.toLowerCase().includes(search.toLowerCase()))
            .slice(pageSize * (page - 1), pageSize * page),
        [combinedVideos, pageSize, page, search]
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
                total={combinedVideos.length}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
                search={search}
                setSearch={setSearch}
            />
            <VideoCards videos={displayedVideos}/>
            <PaginationRow
                name={"video"}
                total={combinedVideos.length}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
            />
        </>
    )
}
