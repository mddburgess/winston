import {useParams} from "react-router";
import {useFindVideoByIdQuery, useListCommentsByVideoIdQuery} from "../../../store/slices/api";
import {Col, Container, Image, Row} from "react-bootstrap";
import {CommentList} from "../../../components/comments/CommentList";

export const VideosIdRoute = () => {
    const {videoId} = useParams();
    const {data: video} = useFindVideoByIdQuery(videoId!)
    const {data: comments} = useListCommentsByVideoIdQuery(videoId!)

    return (
        <Container>
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
