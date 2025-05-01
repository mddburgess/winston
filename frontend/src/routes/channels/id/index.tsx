import { Link, useParams } from "react-router";
import { useFindChannelByHandleQuery } from "../../../store/slices/channels";
import {
    useListVideosByChannelHandleQuery,
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
import { routes } from "../../../utils/links";

export const ChannelDetailsRoute = () => {
    const { channelHandle } = useParams();

    const dispatch = useAppDispatch();

    const [search, setSearch] = useState("");

    const { data: channel } = useFindChannelByHandleQuery(channelHandle!);
    useEffect(() => {
        if (channel) {
            dispatch(initFetchStateForChannel(channel));
        }
    }, [channel]);

    const { data: videos, isSuccess } = useListVideosByChannelHandleQuery(
        channelHandle!,
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
                <BreadcrumbItem linkAs={Link} linkProps={{ to: routes.home }}>
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
