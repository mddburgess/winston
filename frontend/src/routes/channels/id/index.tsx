import { useEffect, useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams, useSearchParams } from "react-router";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { BatchPullCommentsAlert } from "#/routes/channels/id/BatchPullCommentsAlert";
import { BatchPullCommentsSidebar } from "#/routes/channels/id/BatchPullCommentsSidebar";
import { useAppDispatch } from "#/store/hooks";
import { useGetChannelQuery } from "#/store/slices/channels";
import { initFetchStateForChannel } from "#/store/slices/fetches";
import { selectAllVideos, useListVideosQuery } from "#/store/slices/videos";
import { parseIntOrDefault } from "#/utils";
import { routes } from "#/utils/links";
import { ChannelDetails } from "./ChannelDetails";
import { FetchVideosAlert } from "./FetchVideosAlert";
import { VideoCards } from "./VideoCards";

export const ChannelDetailsRoute = () => {
    const { handle } = useParams();

    const dispatch = useAppDispatch();

    const [search, setSearch] = useState("");

    const { data: channel } = useGetChannelQuery({ handle: handle! });
    useEffect(() => {
        if (channel) {
            dispatch(initFetchStateForChannel(channel));
        }
    }, [channel, dispatch]);

    const { data: videos, isSuccess } = useListVideosQuery({
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

    const pageSize = 24;
    const [searchParams] = useSearchParams();
    const pageCount = Math.ceil(filteredVideoList.length / pageSize);
    const pageNumber = Math.max(
        1,
        Math.min(parseIntOrDefault(searchParams.get("p"), 1), pageCount),
    );
    const videosOnPage = useMemo(() => {
        const firstIndex = pageSize * (pageNumber - 1);
        const lastIndex = pageSize * pageNumber;
        return filteredVideoList.slice(firstIndex, lastIndex);
    }, [pageSize, filteredVideoList, pageNumber]);

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
            <BatchPullCommentsAlert videos={videosOnPage} />
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
            <BatchPullCommentsSidebar />
        </>
    );
};
