import { Col, Image, Ratio, Row } from "react-bootstrap";
import { ArrowUpLeftCircleFill, PersonVideo3 } from "react-bootstrap-icons";
import { CommentCounts } from "#/components/comments/CommentCounts";
import { CopyToClipboard } from "#/components/CopyToClipboard";
import { Date } from "#/components/Date";
import { FetchVideoRepliesButton } from "./FetchVideoRepliesButton";
import type { Video } from "#/api";

type VideoDetailsProps = {
    video: Video;
    commentCount: number;
    replyCount: number;
    totalReplyCount: number;
};

export const VideoDetails = ({
    video,
    commentCount,
    replyCount,
    totalReplyCount,
}: VideoDetailsProps) => {
    const videoComments = video.comments ?? {
        comments_disabled: false,
        comment_count: 0,
        reply_count: 0,
        total_reply_count: 0,
        last_fetched_at: undefined,
    };
    const comments = {
        commentsDisabled: videoComments.comments_disabled,
        commentCount: Math.max(videoComments.comment_count, commentCount),
        replyCount: Math.max(videoComments.reply_count, replyCount),
        totalReplyCount: Math.max(
            videoComments.total_reply_count,
            totalReplyCount,
        ),
        lastFetchedAt: videoComments.last_fetched_at,
    };

    return (
        <Row className={"bg-body-tertiary border mx-0 my-3 rounded-3"}>
            <Col xs={12} sm={3} className={"p-0"}>
                <Ratio aspectRatio={"4x3"}>
                    <Image rounded src={video.thumbnail_url} />
                </Ratio>
            </Col>
            <Col xs={12} sm={9} className={"px-3 py-2"}>
                <Row>
                    <Col className={"h3"}>{video.title}</Col>
                    <Col xs={"auto"}>
                        <CopyToClipboard
                            text={`https://www.youtube.com/watch?v=${video.id}`}
                        />
                    </Col>
                </Row>
                <Row className={"pb-2"}>
                    <Col xs={"auto"} className={"align-items-center d-flex"}>
                        <PersonVideo3 className={"me-2"} />
                        {video.channel.title}
                    </Col>
                    <Col xs={"auto"} className={"align-items-center d-flex"}>
                        <ArrowUpLeftCircleFill className={"me-2"} />
                        <Date date={video.published_at} />
                    </Col>
                    <CommentCounts {...comments} />
                    {videoComments.total_reply_count >
                        videoComments.reply_count && (
                        <Col xs={"auto"}>
                            <FetchVideoRepliesButton videoId={video.id} />
                        </Col>
                    )}
                </Row>
                <Row>
                    <Col className={"small"}>{video.description}</Col>
                </Row>
            </Col>
        </Row>
    );
};
