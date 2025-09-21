import { useSearchParams } from "react-router";
import { useListVideosQuery } from "#/api";
import { ChannelVideosListToolbar } from "#/components/videos/ChannelVideosListToolbar";
import { VideoCards } from "#/components/videos/VideoCards";
import { useAppSelector } from "#/store/hooks";
import { parseIntOrDefault } from "#/utils";
import type { ChannelProps } from "#/types";

const ChannelVideosList = ({ channel }: ChannelProps) => {
  const [searchParams] = useSearchParams();
  const pageNumber = parseIntOrDefault(searchParams.get("p"), 1) - 1;

  const pageSize = useAppSelector((state) => state.preferences.videosPageSize);

  const { isFetching, isSuccess, data } = useListVideosQuery({
    handle: channel.handle,
    page: pageNumber,
    size: pageSize,
  });

  return (
    isSuccess && (
      <>
        <ChannelVideosListToolbar
          videos={data.videos}
          totalCount={data.results.total_count}
          pageSize={pageSize}
          pageNumber={pageNumber}
        />
        <VideoCards videos={data.videos} disabled={isFetching} />
      </>
    )
  );
};

export { ChannelVideosList };
