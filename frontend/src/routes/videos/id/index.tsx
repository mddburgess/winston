import {Link, useParams} from "react-router";
import {commentsAdapter, useListCommentsByVideoIdQuery} from "../../../store/slices/comments";
import {useFindVideoByIdQuery} from "../../../store/slices/videos";
import {Breadcrumb, BreadcrumbItem} from "react-bootstrap";
import {CommentList} from "../../../components/comments/CommentList";
import {VideoDetails} from "./VideoDetails";
import {PaginationRow} from "../../../components/PaginationRow";
import {useMemo, useState} from "react";
import {NoCommentsJumbotron} from "./NoCommentsJumbotron";
import {FetchCommentsAlert} from "./FetchCommentsAlert";
import {useAppSelector} from "../../../store/hooks";
import {CommentsDisabledJumbotron} from "./CommentsDisabledJumbotron";

export const VideosIdRoute = () => {
    const {videoId} = useParams();

    const pageSize = 50;
    const [page, setPage] = useState(1);

    const [search, setSearch] = useState("");

    const {data: video} = useFindVideoByIdQuery(videoId!)

    const {isSuccess, data: comments} = useListCommentsByVideoIdQuery(videoId!)
    const commentsList = isSuccess ? commentsAdapter.getSelectors().selectAll(comments) : [];

    const fetchState = useAppSelector(state => state.fetches.comments[videoId!])

    const displayedComments = useMemo(
        () => commentsList.filter(comment =>
            comment.author.displayName.toLowerCase().includes(search.toLowerCase()) ||
            comment.text.toLowerCase().includes(search.toLowerCase()) ||
            comment.replies.filter(reply =>
                    reply.author.displayName.toLowerCase().includes(search.toLowerCase()) ||
                    reply.text.toLowerCase().includes(search.toLowerCase())).length > 0)
            .slice(pageSize * (page - 1), pageSize * page) ?? [],
        [commentsList, pageSize, page, search]
    );

    const commentsDisabled = useMemo(
        () => video?.commentsDisabled || fetchState?.error?.type === "/api/problem/comments-disabled",
        [video, fetchState]
    );

    return (
        <>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{to: "/"}}>
                    Channels
                </BreadcrumbItem>
                {video && <>
                    <BreadcrumbItem linkAs={Link} linkProps={{to: `/channels/${video?.channel.id}`}}>
                        {video?.channel.title}
                    </BreadcrumbItem>
                    <BreadcrumbItem active>
                        {video?.title}
                    </BreadcrumbItem>
                </>}
            </Breadcrumb>
            {video && <VideoDetails video={video}/>}
            {video && !commentsDisabled && commentsList.length == 0 && <NoCommentsJumbotron video={video}/>}
            {video && <FetchCommentsAlert video={video}/>}
            {commentsDisabled && <CommentsDisabledJumbotron/>}
            {commentsList.length > 0 && (
                <>
                    <PaginationRow
                        name={"comment"}
                        total={commentsList.length}
                        pageSize={pageSize}
                        page={page}
                        setPage={setPage}
                        search={search}
                        setSearch={setSearch}
                    />
                    <CommentList comments={displayedComments}/>
                    {commentsList.length > pageSize && <PaginationRow
                        name={"comment"}
                        total={commentsList.length}
                        pageSize={pageSize}
                        page={page}
                        setPage={setPage}
                    />}
                </>
            )}
        </>
    )
}
