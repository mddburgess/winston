import { useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams, useSearchParams } from "react-router";
import { BatchPullCommentsSidebar } from "#/components/channels/BatchPullCommentsSidebar";
import { ChannelDetailsJumbotron } from "#/components/channels/ChannelDetailsJumbotron";
import { PullCommentsRequest } from "#/components/events/PullCommentsRequest";
import { PullVideosRequest } from "#/components/events/PullVideosRequest";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { useGetChannelQuery } from "#/store/slices/channels";
import { pullCommentsRequested } from "#/store/slices/pullComments";
import { clearVideoSelection } from "#/store/slices/selections";
import { selectAllVideos, useListVideosQuery } from "#/store/slices/videos";
import { parseIntOrDefault } from "#/utils";
import { routes } from "#/utils/links";
import { BatchPullCommentsAlert } from "./BatchPullCommentsAlert";
import { VideoCards } from "./VideoCards";

export const ChannelDetailsRoute = () => {
  const dispatch = useAppDispatch();
  const selectedVideos = useAppSelector((state) => state.selections.videos);

  const { handle = "" } = useParams();
  const { data: channel } = useGetChannelQuery({ handle: handle });
  const { data: videos, isSuccess } = useListVideosQuery({ handle: handle });

  const [search, setSearch] = useState("");
  const [showSidebar, setShowSidebar] = useState(false);

  const videoList = useMemo(() => {
    return isSuccess ? selectAllVideos(videos) : [];
  }, [isSuccess, videos]);

  const filteredVideoList = useMemo(() => {
    return videoList.filter((video) => video.title.toLowerCase().includes(search.toLowerCase()));
  }, [videoList, search]);

  const pageSize = 24;
  const [searchParams] = useSearchParams();
  const pageCount = Math.ceil(filteredVideoList.length / pageSize);
  const pageNumber = Math.max(1, Math.min(parseIntOrDefault(searchParams.get("p"), 1), pageCount));
  const videosOnPage = useMemo(() => {
    const firstIndex = pageSize * (pageNumber - 1);
    const lastIndex = pageSize * pageNumber;
    return filteredVideoList.slice(firstIndex, lastIndex);
  }, [pageSize, filteredVideoList, pageNumber]);

  const startPullCommentsBatch = () => {
    const videos = selectedVideos.length > 0 ? selectedVideos : videosOnPage;
    dispatch(pullCommentsRequested(videos));
    dispatch(clearVideoSelection());
    setShowSidebar(true);
  };

  const handleHideSidebar = () => {
    setShowSidebar(false);
  };

  return (
    <>
      <Breadcrumb>
        <BreadcrumbItem linkAs={Link} linkProps={{ to: routes.home }}>
          Channels
        </BreadcrumbItem>
        {channel && <BreadcrumbItem active={true}>{channel.title}</BreadcrumbItem>}
      </Breadcrumb>
      {channel && <ChannelDetailsJumbotron channel={channel} />}
      <BatchPullCommentsAlert onClick={startPullCommentsBatch} />
      <PaginationContext pageSize={24} items={filteredVideoList}>
        {({ pageNumber, setPageNumber, pageSize, pageCount, pageItems, totalItemCount }) => (
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
      <BatchPullCommentsSidebar show={showSidebar} onHide={handleHideSidebar} />
      <PullVideosRequest />
      <PullCommentsRequest />
    </>
  );
};
