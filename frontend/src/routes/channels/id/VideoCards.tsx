import { Row } from "react-bootstrap";
import { VideoCard } from "./VideoCard";
import type { VideoWithChannelIdDto } from "../../../model/VideoDto";

type VideoCardsProps = {
    videos?: VideoWithChannelIdDto[];
};

export const VideoCards = ({ videos = [] }: VideoCardsProps) => (
    <Row xs={1} sm={2} md={3} lg={4} xl={5} xxl={6} className={"px-2 pb-3"}>
        {videos.map((video) => (
            <VideoCard key={video.id} video={video} />
        ))}
    </Row>
);
