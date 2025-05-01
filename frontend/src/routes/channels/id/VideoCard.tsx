import { Card, Col, Ratio, Row } from "react-bootstrap";
import { VideoWithChannelIdDto } from "../../../model/VideoDto";
import { Link } from "react-router";
import { ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Date } from "../../../components/Date";
import { CommentCounts } from "../../../components/comments/CommentCounts";
import { routes } from "../../../utils/links";

type VideoCardProps = {
    video: VideoWithChannelIdDto;
};

export const VideoCard = ({ video }: VideoCardProps) => (
    <Col className={"g-2"}>
        <Card className={"h-100"}>
            <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
                <Link to={routes.videos.details(video)}>
                    <Card.Img variant={"top"} src={video.thumbnailUrl} />
                </Link>
            </Ratio>
            <Card.Body>
                <Card.Subtitle>
                    <Link to={routes.videos.details(video)}>{video.title}</Link>
                </Card.Subtitle>
            </Card.Body>
            <Card.Footer>
                <Row>
                    <CommentCounts
                        comments={video.commentCount}
                        commentsDisabled={video.commentsDisabled}
                        replies={video.replyCount}
                        totalReplies={video.totalReplyCount}
                        showTotalReplies={false}
                    />
                </Row>
            </Card.Footer>
            <Card.Footer className={"d-flex align-items-center"}>
                <ArrowUpLeftCircleFill className={"me-2"} />
                <Date date={video.publishedAt} />
            </Card.Footer>
        </Card>
    </Col>
);
