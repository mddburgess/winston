import {useMemo} from "react";
import {Col, Container, Image, Row} from "react-bootstrap";
import {useParams} from "react-router";
import {useListChannelsQuery} from "../store/slices/api";

export const ChannelVideos = () => {
    const { channelId } = useParams();

    const { data } = useListChannelsQuery()
    const channel = useMemo(() => {
        return data?.find(it => it.id === channelId);
    }, [data]);

    return (
        <Container>
            <h1>Videos</h1>
            <Row>
                <Col>
                    <Image height={200} width={200} src={channel?.thumbnailUrl}/>
                </Col>
                <Col>
                    <h2>{channel?.title}</h2>
                    <h3>{channel?.customUrl}</h3>
                </Col>
            </Row>
        </Container>
    );
}
