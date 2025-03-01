import {useMemo} from "react";
import {Col, Container, Image, ListGroup, Row} from "react-bootstrap";
import {Link, useParams} from "react-router";
import {useListChannelsQuery, useListVideosByChannelIdQuery} from "../store/slices/api";
import {ChatLeftQuoteFill, Download, ReplyAllFill, Upload} from "react-bootstrap-icons";

export const ChannelVideos = () => {
    const {channelId} = useParams();

    const {data: channelData} = useListChannelsQuery()
    const channel = useMemo(() => {
        return channelData?.find(it => it.id === channelId);
    }, [channelData]);

    const {data: videoData = []} = useListVideosByChannelIdQuery(channelId!)
    const renderedVideos = videoData.map(video => (
        <ListGroup.Item key={video.id}>
            <Row>
                <Col>
                    <Image height={72} width={96} src={video.thumbnailUrl}/>
                </Col>
                <Col>
                    <Link to={`/videos/${video.id}`}>{video.title}</Link>
                </Col>
                <Col>
                    {video.commentCount} <ChatLeftQuoteFill/>
                </Col>
                <Col>
                    {video.replyCount} / {video.totalReplyCount} <ReplyAllFill/>
                </Col>
                <Col>
                    <Upload/> {video.publishedAt}<br/>
                    <Download/> {video.lastFetchedAt}
                </Col>
            </Row>
        </ListGroup.Item>
    ))

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
            <ListGroup>
                {renderedVideos}
            </ListGroup>
        </Container>
    );
}
