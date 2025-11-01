import { useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { useGetVideoQuery } from "#/api";
import { CommentList } from "#/components/comments/CommentList";
import { PullCommentsRequest } from "#/components/events/PullCommentsRequest";
import { PullRepliesRequest } from "#/components/events/PullRepliesRequest";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { NoCommentsJumbotron } from "#/components/videos/NoCommentsJumbotron";
import { VideoDetailsJumbotron } from "#/components/videos/VideoDetailsJumbotron";
import { selectAllReplies, selectAllTopLevelComments, useListCommentsQuery } from "#/store/slices/comments";
import { routes } from "#/utils/links";
import { CommentsDisabledJumbotron } from "./CommentsDisabledJumbotron";

export const VideoDetailsRoute = () => {
  const { videoId = "" } = useParams();

  const [search, setSearch] = useState("");

  const { data: video } = useGetVideoQuery({ id: videoId });

  const { isSuccess, data: comments } = useListCommentsQuery({
    id: videoId,
  });
  const commentsList = useMemo(() => {
    return isSuccess ? selectAllTopLevelComments(comments) : [];
  }, [isSuccess, comments]);

  const filteredComments = useMemo(
    () =>
      commentsList.filter(
        (comment) =>
          comment.author.handle.toLowerCase().includes(search.toLowerCase()) ||
          comment.text.original.toLowerCase().includes(search.toLowerCase()) ||
          selectAllReplies(comment.replies).filter(
            (reply) =>
              reply.author.handle.toLowerCase().includes(search.toLowerCase()) ||
              reply.text.original.toLowerCase().includes(search.toLowerCase()),
          ).length > 0,
      ),
    [commentsList, search],
  );

  const commentsDisabled = useMemo(() => video?.comments?.comments_disabled, [video]);

  return (
    <>
      <Breadcrumb>
        <BreadcrumbItem linkAs={Link} linkProps={{ to: routes.home }}>
          Channels
        </BreadcrumbItem>
        {video && (
          <>
            <BreadcrumbItem
              linkAs={Link}
              linkProps={{
                to: routes.channels.details(video.channel.handle),
              }}
            >
              {video.channel.title}
            </BreadcrumbItem>
            <BreadcrumbItem active>{video.title}</BreadcrumbItem>
          </>
        )}
      </Breadcrumb>
      {video && <VideoDetailsJumbotron video={video} />}
      {video && !commentsDisabled && commentsList.length == 0 && <NoCommentsJumbotron video={video} />}
      {commentsDisabled && <CommentsDisabledJumbotron />}
      {commentsList.length > 0 && (
        <PaginationContext pageSize={50} items={filteredComments}>
          {({ pageNumber, setPageNumber, pageSize, pageCount, pageItems, totalItemCount }) => (
            <>
              <PaginationRow
                name={"comment"}
                total={totalItemCount}
                pageSize={pageSize}
                page={pageNumber}
                setPage={setPageNumber}
                search={search}
                setSearch={setSearch}
              />
              <CommentList comments={pageItems} />
              {pageCount > 1 && (
                <PaginationRow
                  name={"comment"}
                  total={totalItemCount}
                  pageSize={pageSize}
                  page={pageNumber}
                  setPage={setPageNumber}
                />
              )}
            </>
          )}
        </PaginationContext>
      )}
      <PullCommentsRequest />
      <PullRepliesRequest />
    </>
  );
};
