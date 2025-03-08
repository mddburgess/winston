import {Card, Col} from "react-bootstrap";
import {VideoWithChannelIdDto} from "../../../model/VideoDto";
import {Link} from "react-router";
import {ArrowUpLeftCircleFill} from "react-bootstrap-icons";
import {Date} from "../../../components/Date";

type VideoCardProps = {
    video: VideoWithChannelIdDto
}

export const VideoCard = ({video}: VideoCardProps) => (
    <Col className={"g-2"}>
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
                <Date date={video.publishedAt}/>
            </Card.Footer>
        </Card>
    </Col>
)
