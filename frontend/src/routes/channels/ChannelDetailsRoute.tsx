import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { BatchPullCommentsSidebar } from "#/components/channels/BatchPullCommentsSidebar";
import { ChannelDetailsJumbotron } from "#/components/channels/ChannelDetailsJumbotron";
import { PullCommentsRequest } from "#/components/events/PullCommentsRequest";
import { PullVideosRequest } from "#/components/events/PullVideosRequest";
import { ChannelVideosList } from "#/components/videos/ChannelVideosList";
import { useGetChannelQuery } from "#/store/slices/channels";
import { routes } from "#/utils/links";

const ChannelDetailsRoute = () => {
  const { handle = "" } = useParams();
  const { data: channel } = useGetChannelQuery({ handle: handle });

  return (
    <>
      <Breadcrumb>
        <BreadcrumbItem linkAs={Link} linkProps={{ to: routes.home }}>
          Channels
        </BreadcrumbItem>
        {channel && <BreadcrumbItem active={true}>{channel.title}</BreadcrumbItem>}
      </Breadcrumb>
      {channel && <ChannelDetailsJumbotron channel={channel} />}
      {channel && <ChannelVideosList channel={channel} />}
      <BatchPullCommentsSidebar />
      <PullVideosRequest />
      <PullCommentsRequest />
    </>
  );
};

export { ChannelDetailsRoute };
