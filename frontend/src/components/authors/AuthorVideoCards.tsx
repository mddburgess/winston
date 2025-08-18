import { keyBy } from "lodash";
import { Card, Col, Ratio, Row } from "react-bootstrap";
import { ClockHistory } from "react-bootstrap-icons";
import { useSearchParams } from "react-router";
import { CommentCounts } from "#/components/comments/CommentCounts";
import { Date } from "#/components/Date";
import { VideoPublishedAt } from "#/components/videos/VideoPublishedAt";
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

const AuthorVideoCard = ({ video, statistics }: AuthorVideoCardProps) => {
    const setSearchParams = useSearchParams()[1];

    const handleClick = () => {
        setSearchParams({ v: video.id });
    };

    return (
        <Col className={"g-2"}>
            <Card
                className={"cursor-pointer h-100 hover-bg-info-subtle"}
                onClick={handleClick}
            >
                <Ratio aspectRatio={"4x3"} className={"bg-secondary-subtle"}>
                    <Card.Img
                        variant={"top"}
                        src={video.thumbnail_url}
                    ></Card.Img>
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
                <Card.Footer>
                    <VideoPublishedAt video={video} />
                </Card.Footer>
            </Card>
        </Col>
    );
};

export { AuthorVideoCards };
