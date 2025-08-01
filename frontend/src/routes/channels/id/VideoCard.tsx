import { Card, Col, Ratio, Row } from "react-bootstrap";
import { ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { CommentCounts } from "#/components/comments/CommentCounts";
import { Date } from "#/components/Date";
import { routes } from "#/utils/links";
import type { Video } from "#/api";

type Props = {
    video: Video;
};

export const VideoCard = ({ video }: Props) => (
    <Col className={"g-2"}>
        <Card className={"h-100"}>
            <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
                <Link to={routes.videos.details(video.id)}>
                    <Card.Img variant={"top"} src={video.thumbnail_url} />
                </Link>
            </Ratio>
            <Card.Body>
                <Card.Subtitle>
                    <Link to={routes.videos.details(video.id)}>
                        {video.title}
                    </Link>
                </Card.Subtitle>
            </Card.Body>
            <Card.Footer>
                <Row>
                    <CommentCounts
                        commentsDisabled={video.comments?.comments_disabled}
                        commentCount={video.comments?.comment_count}
                        replyCount={video.comments?.reply_count}
                        totalReplyCount={video.comments?.total_reply_count}
                        lastFetchedAt={video.comments?.last_fetched_at}
                        showTotalReplyCount={false}
                    />
                </Row>
            </Card.Footer>
            <Card.Footer className={"d-flex align-items-center"}>
                <ArrowUpLeftCircleFill className={"me-2"} />
                <Date date={video.published_at} />
            </Card.Footer>
        </Card>
    </Col>
);
