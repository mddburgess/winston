import { keyBy } from "lodash";
import { Card, Col, Ratio, Row } from "react-bootstrap";
import { ArrowUpLeftCircleFill, ClockHistory } from "react-bootstrap-icons";
import { CommentCounts } from "#/components/comments/CommentCounts";
import { Date } from "#/components/Date";
import type {
    AuthorProps,
    VideoListProps,
    VideoProps,
    VideoStatistics,
} from "#/types";

type AuthorVideoCardsProps = AuthorProps & VideoListProps;

const AuthorVideoCards = ({ author, videos }: AuthorVideoCardsProps) => {
    const videoStatistics = keyBy(author.video_statistics, "video_id");

    return (
        <Row xs={1} sm={2} md={3} lg={4} xl={5} xxl={6} className={"px-2 pb-3"}>
            {videos.map((video) => (
                <AuthorVideoCard
                    key={video.id}
                    video={video}
                    statistics={videoStatistics[video.id]}
                />
            ))}
        </Row>
    );
};

type AuthorVideoCardProps = VideoProps & { statistics: VideoStatistics };

const AuthorVideoCard = ({ video, statistics }: AuthorVideoCardProps) => (
    <Col className={"g-2"}>
        <Card className={"h-100"}>
            <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
                <Card.Img variant={"top"} src={video.thumbnail_url}></Card.Img>
            </Ratio>
            <Card.Body>
                <Card.Subtitle>{video.title}</Card.Subtitle>
            </Card.Body>
            <Card.Footer>
                <Row>
                    <CommentCounts
                        commentCount={statistics.comment_count}
                        replyCount={statistics.reply_count}
                        totalReplyCount={statistics.reply_count}
                        lastFetchedAt={statistics.last_commented_at}
                    />
                </Row>
                <Row>
                    <Col className={"flex-center"}>
                        <ClockHistory className={"me-2"} />
                        <Date date={statistics.last_commented_at} />
                    </Col>
                </Row>
            </Card.Footer>
            <Card.Footer className={"bg-body-secondary"}>
                <Row>
                    <Col xs={12} className={"flex-center"}>
                        <ArrowUpLeftCircleFill className={"me-2"} />
                        <Date date={video.published_at} />
                    </Col>
                </Row>
            </Card.Footer>
        </Card>
    </Col>
);

export { AuthorVideoCards };
