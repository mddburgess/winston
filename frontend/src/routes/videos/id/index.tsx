import {Link, useParams} from "react-router";
import {useFindVideoByIdQuery, useListCommentsByVideoIdQuery} from "../../../store/slices/api";
import {Breadcrumb, BreadcrumbItem, Col, Container, Image, Row} from "react-bootstrap";
import {CommentList} from "../../../components/comments/CommentList";
import {VideoDetails} from "./VideoDetails";
import {PaginationRow} from "../../../components/PaginationRow";
import {useMemo, useState} from "react";
import {NoCommentsJumbotron} from "./NoCommentsJumbotron";
import {FetchCommentsAlert} from "./FetchCommentsAlert";
import {useAppSelector} from "../../../store/hooks";
import {DateTime} from "luxon";
import {descBy} from "../../../utils";
import {CommentsDisabledJumbotron} from "./CommentsDisabledJumbotron";

export const VideosIdRoute = () => {
    const {videoId} = useParams();

    const pageSize = 50;
    const [page, setPage] = useState(1);

    const [search, setSearch] = useState("");

    const {data: video} = useFindVideoByIdQuery(videoId!)

    const {data: comments} = useListCommentsByVideoIdQuery(videoId!)
    const fetchState = useAppSelector(state => state.fetches.comments[videoId!])
    const combinedComments = useMemo(
        () => (fetchState?.data ?? []).concat(comments ?? [])
            .sort(descBy(comment => DateTime.fromISO(comment.publishedAt).valueOf())),
        [comments, fetchState]
    );

    const displayedComments = useMemo(
        () => combinedComments.filter(comment =>
            comment.author.displayName.toLowerCase().includes(search.toLowerCase()) ||
            comment.text.toLowerCase().includes(search.toLowerCase()) ||
            comment.replies.filter(reply =>
                    reply.author.displayName.toLowerCase().includes(search.toLowerCase()) ||
                    reply.text.toLowerCase().includes(search.toLowerCase())).length > 0)
            .slice(pageSize * (page - 1), pageSize * page) ?? [],
        [combinedComments, pageSize, page, search]
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
            {video && !commentsDisabled && (combinedComments?.length ?? 0) == 0 && <NoCommentsJumbotron video={video}/>}
            {video && <FetchCommentsAlert video={video}/>}
            {commentsDisabled && <CommentsDisabledJumbotron/>}
            {(combinedComments?.length ?? 0) > 0 && (
                <>
                    <PaginationRow
                        name={"comment"}
                        total={combinedComments?.length ?? 0}
                        pageSize={pageSize}
                        page={page}
                        setPage={setPage}
                        search={search}
                        setSearch={setSearch}
                    />
                    <CommentList comments={displayedComments}/>
                    {(combinedComments?.length ?? 0) > pageSize && <PaginationRow
                        name={"comment"}
                        total={combinedComments?.length ?? 0}
                        pageSize={pageSize}
                        page={page}
                        setPage={setPage}
                    />}
                </>
            )}
        </>
    )
}
