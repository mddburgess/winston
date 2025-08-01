import { useMemo, useState } from "react";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { Link, useParams } from "react-router";
import { CommentList } from "#/components/comments/CommentList";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import { useAppSelector } from "#/store/hooks";
import {
    selectAllReplies,
    selectAllTopLevelComments,
    selectReplyCount,
    useListCommentsQuery,
} from "#/store/slices/comments";
import { useGetVideoQuery } from "#/store/slices/videos";
import { sumBy } from "#/utils";
import { routes } from "#/utils/links";
import { CommentsDisabledJumbotron } from "./CommentsDisabledJumbotron";
import { FetchCommentsAlert } from "./FetchCommentsAlert";
import { NoCommentsJumbotron } from "./NoCommentsJumbotron";
import { VideoDetails } from "./VideoDetails";

export const VideoDetailsRoute = () => {
    const { videoId } = useParams();

    const [search, setSearch] = useState("");

    const { data: video } = useGetVideoQuery({ id: videoId! });

    const { isSuccess, data: comments } = useListCommentsQuery({
        id: videoId!,
    });
    const commentsList = useMemo(() => {
        return isSuccess ? selectAllTopLevelComments(comments) : [];
    }, [isSuccess, comments]);

    const fetchState = useAppSelector(
        (state) => state.fetches.comments[videoId!],
    );

    const filteredComments = useMemo(
        () =>
            commentsList.filter(
                (comment) =>
                    comment.author.handle
                        .toLowerCase()
                        .includes(search.toLowerCase()) ||
                    comment.text.toLowerCase().includes(search.toLowerCase()) ||
                    selectAllReplies(comment.replies).filter(
                        (reply) =>
                            reply.author.handle
                                .toLowerCase()
                                .includes(search.toLowerCase()) ||
                            reply.text
                                .toLowerCase()
                                .includes(search.toLowerCase()),
                    ).length > 0,
            ),
        [commentsList, search],
    );

    const replyCount = sumBy(commentsList, (comment) =>
        selectReplyCount(comment.replies),
    );
    const totalReplyCount = sumBy(
        commentsList,
        (comment) => comment.total_reply_count,
    );

    const commentsDisabled = useMemo(
        () => video?.comments?.comments_disabled,
        [video],
    );

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
                                to: routes.channels.details(
                                    video.channel.handle,
                                ),
                            }}
                        >
                            {video.channel.title}
                        </BreadcrumbItem>
                        <BreadcrumbItem active>{video.title}</BreadcrumbItem>
                    </>
                )}
            </Breadcrumb>
            {video && (
                <VideoDetails
                    video={video}
                    commentCount={commentsList.length}
                    replyCount={replyCount}
                    totalReplyCount={totalReplyCount}
                />
            )}
            {video && !commentsDisabled && commentsList.length == 0 && (
                <NoCommentsJumbotron video={video} />
            )}
            {video && <FetchCommentsAlert video={video} />}
            {commentsDisabled && <CommentsDisabledJumbotron />}
            {commentsList.length > 0 && (
                <PaginationContext pageSize={50} items={filteredComments}>
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
        </>
    );
};
