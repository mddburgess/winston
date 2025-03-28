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

export const VideosIdRoute = () => {
    const {videoId} = useParams();

    const pageSize = 50;
    const [page, setPage] = useState(1);

    const {data: video} = useFindVideoByIdQuery(videoId!)

    const {data: comments} = useListCommentsByVideoIdQuery(videoId!)
    const fetchedComments = useAppSelector(state => state.fetches.comments[videoId!]?.data)
    const combinedComments = useMemo(
        () => (fetchedComments ?? []).concat(comments ?? [])
            .sort((a, b) => DateTime.fromISO(b.publishedAt).valueOf() - DateTime.fromISO(a.publishedAt).valueOf()),
        [comments, fetchedComments]
    );

    const displayedComments = useMemo(
        () => combinedComments.slice(pageSize * (page - 1), pageSize * page) ?? [],
        [combinedComments, pageSize, page]
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
            {video && (combinedComments?.length ?? 0) == 0 && <NoCommentsJumbotron video={video}/>}
            {video && <FetchCommentsAlert video={video}/>}
            {(combinedComments?.length ?? 0) > 0 && (
                <>
                    <PaginationRow
                        name={"comment"}
                        total={combinedComments?.length ?? 0}
                        pageSize={pageSize}
                        page={page}
                        setPage={setPage}
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
