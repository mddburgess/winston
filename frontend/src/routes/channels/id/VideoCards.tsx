import {VideoWithChannelIdDto} from "../../../model/VideoDto";
import {Row} from "react-bootstrap";
import {VideoCard} from "./VideoCard";

type VideoCardsProps = {
    videos?: VideoWithChannelIdDto[]
}

export const VideoCards = ({videos = []}: VideoCardsProps) => (
    <Row xs={1} sm={2} md={3} lg={4} xl={5} xxl={6}>
        {videos.map(video => (<VideoCard video={video}/>))}
    </Row>
)
