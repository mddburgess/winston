import {Card, Col} from "react-bootstrap";
import {VideoDto} from "../../../model/VideoDto";
import {Link} from "react-router";
import {DateTime} from "luxon";
import {ArrowUpLeftCircleFill} from "react-bootstrap-icons";

type VideoCardProps = {
    video: VideoDto
}

export const VideoCard = ({video}: VideoCardProps) => (
    <Col className={"mb-2 px-1"}>
        <Card className={"h-100"}>
            <Card.Img variant={"top"} src={video.thumbnailUrl}/>
            <Card.Body>
                <Card.Subtitle>
                    <Link to={`/videos/${video.id}`}>
                        {video.title}
                    </Link>
                </Card.Subtitle>
            </Card.Body>
            <Card.Footer className={"d-flex align-items-center"}>
                <ArrowUpLeftCircleFill className={"me-2"}/>
                {DateTime.fromISO(video.publishedAt!).toRelative()}
            </Card.Footer>
        </Card>
    </Col>
)
