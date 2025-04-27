import { Link, useParams } from "react-router";
import { useFindChannelByIdQuery } from "../../../store/slices/channels";
import {
    useListVideosByChannelIdQuery,
    videosAdapter,
} from "../../../store/slices/videos";
import { useEffect, useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { ChannelDetails } from "./ChannelDetails";
import { VideoCards } from "./VideoCards";
import { FetchVideosAlert } from "./FetchVideosAlert";
import { useAppDispatch } from "../../../store/hooks";
import { initFetchStateForChannel } from "../../../store/slices/fetches";
import { PaginationRow } from "../../../components/PaginationRow";
import { PaginationContext } from "../../../components/PaginationContext";

export const ChannelsIdRoute = () => {
    const { channelId } = useParams();

    const dispatch = useAppDispatch();

    const [search, setSearch] = useState("");

    const { data: channel } = useFindChannelByIdQuery(channelId!);
    useEffect(() => {
        if (channel) {
            dispatch(initFetchStateForChannel(channel));
        }
    }, [channel]);

    const { data: videos, isSuccess } = useListVideosByChannelIdQuery(
        channelId!,
    );
    const videoList = isSuccess
        ? videosAdapter.getSelectors().selectAll(videos)
        : [];

    const filteredVideoList = useMemo(() => {
        return videoList.filter((video) =>
            video.title.toLowerCase().includes(search.toLowerCase()),
        );
    }, [videoList, search]);

    return (
        <>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{ to: "/" }}>
                    Channels
                </BreadcrumbItem>
                {channel && (
                    <BreadcrumbItem active={true}>
                        {channel.title}
                    </BreadcrumbItem>
                )}
            </Breadcrumb>
            {channel && <ChannelDetails channel={channel} />}
            {channel && <FetchVideosAlert channel={channel} />}
            <PaginationContext pageSize={24} items={filteredVideoList}>
                {({
                    pageNumber,
                    setPageNumber,
                    pageSize,
                    pageCount,
                    pageItems,
                    totalItemCount,
                }) => (
                    <>
                        <PaginationRow
                            name={"video"}
                            total={totalItemCount}
                            pageSize={pageSize}
                            page={pageNumber}
                            setPage={setPageNumber}
                            search={search}
                            setSearch={setSearch}
                        />
                        <VideoCards videos={pageItems} />
                        {pageCount > 1 && (
                            <PaginationRow
                                name={"video"}
                                total={totalItemCount}
                                pageSize={pageSize}
                                page={pageNumber}
                                setPage={setPageNumber}
                            />
                        )}
                    </>
                )}
            </PaginationContext>
        </>
    );
};
