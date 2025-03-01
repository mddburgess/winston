import {useParams} from "react-router";
import {Col, Container, Image, Row} from "react-bootstrap";
import {useFindVideoByIdQuery} from "../store/slices/api";

export const Video = () => {
    const {videoId} = useParams();

    const {data} = useFindVideoByIdQuery(videoId!)

    return (
        <Container>
            <Row>
                <Col>
                    <Image width={480} height={360} src={data?.thumbnailUrl}/>
                </Col>
                <Col>
                    <h2>{data?.title}</h2>
                    <h3>{data?.channel?.title}</h3>
                    {data?.description}
                </Col>
            </Row>
        </Container>
    );
};
