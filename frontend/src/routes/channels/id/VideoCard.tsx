import { Card, Col, Ratio, Row } from "react-bootstrap";
import { ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { CommentCounts } from "#/components/comments/CommentCounts";
import { Date } from "#/components/Date";
import { routes } from "#/utils/links";
import type { VideoProps } from "#/types";

export const VideoCard = ({ video }: VideoProps) => (
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
                        {...video.comments}
                        showTotalReplyCount={false}
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
