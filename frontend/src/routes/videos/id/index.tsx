import {Link, useParams} from "react-router";
import {useFindVideoByIdQuery, useListCommentsByVideoIdQuery} from "../../../store/slices/api";
import {Breadcrumb, BreadcrumbItem, Col, Container, Image, Row} from "react-bootstrap";
import {CommentList} from "../../../components/comments/CommentList";
import {VideoDetails} from "./VideoDetails";
import {PaginationRow} from "../../../components/PaginationRow";
import {useMemo, useState} from "react";

export const VideosIdRoute = () => {
    const {videoId} = useParams();
    const {data: video} = useFindVideoByIdQuery(videoId!)
    const {data: comments} = useListCommentsByVideoIdQuery(videoId!)

    const pageSize = 50;
    const [page, setPage] = useState(1);
    const displayedComments = useMemo(
        () => comments?.slice(pageSize * (page - 1), pageSize * page) ?? [],
        [comments, pageSize, page]
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
            <PaginationRow
                name={"comment"}
                total={comments?.length ?? 0}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
            />
            <CommentList comments={displayedComments}/>
            {(comments?.length ?? 0) > pageSize && <PaginationRow
                name={"comment"}
                total={comments?.length ?? 0}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
            />}
        </>
    )
}
