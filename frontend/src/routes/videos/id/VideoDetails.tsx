import {VideoWithChannelDto} from "../../../model/VideoDto";
import {Col, Image, Ratio, Row} from "react-bootstrap";
import {Date} from "../../../components/Date";
import {ArrowUpLeftCircleFill, PersonVideo3} from "react-bootstrap-icons";
import {CopyToClipboard} from "../../../components/CopyToClipboard";
import {CommentCounts} from "../../../components/comments/CommentCounts";
import {FetchVideoRepliesButton} from "./FetchVideoRepliesButton";

type VideoDetailsProps = {
    video: VideoWithChannelDto;
}

export const VideoDetails = ({video}: VideoDetailsProps) => (
    <Row className={"bg-body-tertiary border mx-0 my-3 rounded-3"}>
        <Col xs={12} sm={3} className={"p-0"}>
            <Ratio aspectRatio={"4x3"}>
                <Image rounded src={video.thumbnailUrl}/>
            </Ratio>
        </Col>
        <Col xs={12} sm={9} className={"px-3 py-2"}>
            <Row>
                <Col className={"h3"}>
                    {video.title}
                </Col>
                <Col xs={"auto"}>
                    <CopyToClipboard text={`https://www.youtube.com/watch?v=${video.id}`}/>
                </Col>
            </Row>
            <Row className={"pb-2"}>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <PersonVideo3 className={"me-2"}/>
                    {video.channel.title}
                </Col>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <ArrowUpLeftCircleFill className={"me-2"}/>
                    <Date date={video.publishedAt}/>
                </Col>
                <CommentCounts
                    comments={video.commentCount}
                    commentsDisabled={video.commentsDisabled}
                    replies={video.replyCount}
                    totalReplies={video.totalReplyCount}
                />
                {video.totalReplyCount > video.replyCount && (
                    <Col xs={"auto"}>
                        <FetchVideoRepliesButton videoId={video.id}/>
                    </Col>
                )}
            </Row>
            <Row>
                <Col className={"small"}>
                    {video.description}
                </Col>
            </Row>
        </Col>
    </Row>
)
