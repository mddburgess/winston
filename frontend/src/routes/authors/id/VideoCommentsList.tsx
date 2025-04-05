import {VideoWithChannelIdDto} from "../../../model/VideoDto";
import {CommentDto} from "../../../model/CommentDto";
import {Col, Row} from "react-bootstrap";
import {VideoCard} from "../../channels/id/VideoCard";
import {CommentList} from "../../../components/comments/CommentList";
import {v4 as uuidv4} from "uuid";
import {DateTime} from "luxon";
import {descBy} from "../../../utils";
import {AuthorDto} from "../../../model/authors/AuthorDto";
import {AuthorDetailsResponse} from "../../../model/authors/AuthorDetailsResponse";

type VideoCommentsListProps = AuthorDetailsResponse;

export const VideoCommentsList = ({author, videos, comments}: VideoCommentsListProps) => {

    const videoCommentsList = [...videos]
        .sort(descBy(video => DateTime.fromISO(video.publishedAt).valueOf()))
        .map(video => ({
            video: video,
            comments: comments.filter(comment => comment.videoId === video.id),
        }));

    return (
        <>
            {videoCommentsList.map(videoComments => (
                <VideoCommentsListItem key={uuidv4()} author={author} {...videoComments}/>
            ))}
        </>
    );
}

type VideoCommentsListItemProps = {
    author: AuthorDto,
    video: VideoWithChannelIdDto;
    comments: CommentDto[];
}

const VideoCommentsListItem = ({author, video, comments}: VideoCommentsListItemProps) => (
    <Row>
        <Col xs={"auto"} lg={3} xxl={2}>
            <div className={"mb-3"}>
                <VideoCard video={video}/>
            </div>
        </Col>
        <Col xs={"auto"} lg={9} xxl={10}>
            <CommentList comments={comments} highlightAuthorId={author.id} />
        </Col>
    </Row>
)
