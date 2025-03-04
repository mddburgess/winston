import {Link, useParams} from "react-router";
import {useFindVideoByIdQuery, useListCommentsByVideoIdQuery} from "../../../store/slices/api";
import {Breadcrumb, BreadcrumbItem, Col, Container, Image, Row} from "react-bootstrap";
import {CommentList} from "../../../components/comments/CommentList";

export const VideosIdRoute = () => {
    const {videoId} = useParams();
    const {data: video} = useFindVideoByIdQuery(videoId!)
    const {data: comments} = useListCommentsByVideoIdQuery(videoId!)

    return (
        <Container>
            <Breadcrumb>
                <BreadcrumbItem linkAs={Link} linkProps={{to: "/"}}>
                    Channels
                </BreadcrumbItem>
                <BreadcrumbItem linkAs={Link} linkProps={{to: `/channels/${video?.channel?.id}`}}>
                    {video?.channel?.title}
                </BreadcrumbItem>
                <BreadcrumbItem active>
                    {video?.title}
                </BreadcrumbItem>
            </Breadcrumb>
            <Row>
                <Col>
                    <Image width={480} height={360} src={video?.thumbnailUrl}/>
                </Col>
                <Col>
                    <h1>{video?.title}</h1>
                    <h2>{video?.channel?.title}</h2>
                    {video?.description}
                </Col>
            </Row>
            <CommentList comments={comments}/>
        </Container>
    )
}
