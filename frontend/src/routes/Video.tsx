import {useParams} from "react-router";
import {Col, Container, Image, Row} from "react-bootstrap";
import {useFindVideoByIdQuery} from "../store/slices/api";
import {CommentList} from "../components/comments/CommentList";

export const Video = () => {
    const {videoId} = useParams();

    const {data: video} = useFindVideoByIdQuery(videoId!)



    return (
        <Container>
            <Row>
                <Col>
                    <Image width={480} height={360} src={video?.thumbnailUrl}/>
                </Col>
                <Col>
                    <h2>{video?.title}</h2>
                    <h3>{video?.channel?.title}</h3>
                    {video?.description}
                </Col>
            </Row>
            <CommentList videoId={videoId} />
        </Container>
    );
};
