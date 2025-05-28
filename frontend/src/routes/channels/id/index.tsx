import { useEffect, useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { useAppDispatch } from "#/store/hooks";
import { useGetChannelByHandleQuery } from "#/store/slices/channels";
import { initFetchStateForChannel } from "#/store/slices/fetches";
import {
    selectAllVideos,
    useListVideosForChannelQuery,
} from "#/store/slices/videos";
import { routes } from "#/utils/links";
import { ChannelDetails } from "./ChannelDetails";
import { FetchVideosAlert } from "./FetchVideosAlert";
import { VideoCards } from "./VideoCards";

export const ChannelDetailsRoute = () => {
    const { handle } = useParams();

    const dispatch = useAppDispatch();

    const [search, setSearch] = useState("");

    const { data: channel } = useGetChannelByHandleQuery({ handle: handle! });
    useEffect(() => {
        if (channel) {
            dispatch(initFetchStateForChannel(channel));
        }
    }, [channel, dispatch]);

    const { data: videos, isSuccess } = useListVideosForChannelQuery({
        handle: handle!,
    });

    const videoList = useMemo(() => {
        return isSuccess ? selectAllVideos(videos) : [];
    }, [isSuccess, videos]);

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
