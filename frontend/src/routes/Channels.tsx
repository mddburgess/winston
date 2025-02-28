import {Col, Container, Image, ListGroup, Row} from "react-bootstrap";
import {ArrowClockwise, Youtube} from "react-bootstrap-icons";
import {useListChannelsQuery} from "../store/slices/api";

export const Channels = () => {
    const { data = [] } = useListChannelsQuery()

    const renderedChannels = data.map(channel => (
        <ListGroup.Item key={channel.id}>
            <Row>
                <Col>
                    <Image height={64} width={64} src={channel.thumbnailUrl}/>
                </Col>
                <Col>
                    <h5>{channel.title}</h5>
                    <small>{channel.customUrl}</small>
                </Col>
                <Col>
                    {channel.videoCount} <Youtube/>
                </Col>
                <Col>
                    {channel.publishedAt}
                </Col>
                <Col>
                    {channel.lastFetchedAt} <ArrowClockwise/>
                </Col>
            </Row>
        </ListGroup.Item>
    ));

    return (
        <Container>
            <h1>Channels</h1>
            <ListGroup>
                {renderedChannels}
            </ListGroup>
        </Container>
    );
};
