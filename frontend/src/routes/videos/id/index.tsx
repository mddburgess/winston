import { Link, useParams } from "react-router";
import {
    commentsAdapter,
    repliesAdapter,
    useListCommentsByVideoIdQuery,
} from "../../../store/slices/comments";
import { useFindVideoByIdQuery } from "../../../store/slices/videos";
import { Breadcrumb, BreadcrumbItem } from "react-bootstrap";
import { CommentList } from "../../../components/comments/CommentList";
import { VideoDetails } from "./VideoDetails";
import { PaginationRow } from "../../../components/PaginationRow";
import { useMemo, useState } from "react";
import { NoCommentsJumbotron } from "./NoCommentsJumbotron";
import { FetchCommentsAlert } from "./FetchCommentsAlert";
import { useAppSelector } from "../../../store/hooks";
import { CommentsDisabledJumbotron } from "./CommentsDisabledJumbotron";
import { PaginationContext } from "../../../components/PaginationContext";

export const VideosIdRoute = () => {
    const { videoId } = useParams();

    const [search, setSearch] = useState("");

    const { data: video } = useFindVideoByIdQuery(videoId!);

    const { isSuccess, data: comments } = useListCommentsByVideoIdQuery(
        videoId!,
    );
    const commentsList = isSuccess
        ? commentsAdapter.getSelectors().selectAll(comments)
        : [];

    const fetchState = useAppSelector(
        (state) => state.fetches.comments[videoId!],
    );

    const filteredComments = useMemo(
        () =>
            commentsList.filter(
                (comment) =>
                    comment.author.displayName
                        .toLowerCase()
                        .includes(search.toLowerCase()) ||
                    comment.text.toLowerCase().includes(search.toLowerCase()) ||
                    repliesAdapter
                        .getSelectors()
                        .selectAll(comment.replies)
                        .filter(
                            (reply) =>
                                reply.author.displayName
                                    .toLowerCase()
                                    .includes(search.toLowerCase()) ||
                                reply.text
                                    .toLowerCase()
                                    .includes(search.toLowerCase()),
                        ).length > 0,
            ),
        [commentsList, search],
    );

    const commentsDisabled = useMemo(
        () => video?.commentsDisabled,
        [video, fetchState],
    );

    return (
        <>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{ to: "/" }}>
                    Channels
                </BreadcrumbItem>
                {video && (
                    <>
                        <BreadcrumbItem
                            linkAs={Link}
                            linkProps={{ to: `/channels/${video?.channel.id}` }}
                        >
                            {video?.channel.title}
                        </BreadcrumbItem>
                        <BreadcrumbItem active>{video?.title}</BreadcrumbItem>
                    </>
                )}
            </Breadcrumb>
            {video && <VideoDetails video={video} />}
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
