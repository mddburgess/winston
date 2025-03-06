import {Card, Col} from "react-bootstrap";
import {VideoDto} from "../../../model/VideoDto";
import {Link} from "react-router";

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
            <Card.Footer>
                {video.publishedAt}
            </Card.Footer>
        </Card>
    </Col>
)
