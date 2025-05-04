import type { VideoListProps } from "../../../types";
import { Row } from "react-bootstrap";
import { VideoCard } from "./VideoCard";

export const VideoCards = ({ videos }: VideoListProps) => (
    <Row xs={1} sm={2} md={3} lg={4} xl={5} xxl={6} className={"px-2 pb-3"}>
        {videos.map((video) => (
            <VideoCard key={video.id} video={video} />
        ))}
    </Row>
);
