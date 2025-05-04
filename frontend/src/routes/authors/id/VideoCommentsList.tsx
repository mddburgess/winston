import type {
    Author,
    AuthorDetailsResponse,
    Comment,
    Video,
} from "../../../types";
import { Col, Row } from "react-bootstrap";
import { VideoCard } from "../../channels/id/VideoCard";
import { CommentList } from "../../../components/comments/CommentList";
import { v4 as uuidv4 } from "uuid";
import { DateTime } from "luxon";
import { descBy } from "../../../utils";
import type { CommentState } from "../../../store/slices/comments";
import { repliesAdapter } from "../../../store/slices/comments";

type VideoCommentsListProps = AuthorDetailsResponse;

type VideoCommentsListItemProps = {
    author: Author;
    video: Video;
    comments: Comment[];
};

const VideoCommentsListItem = ({
    author,
    video,
    comments,
}: VideoCommentsListItemProps) => {
    const commentList: CommentState[] = comments.map((comment) => ({
        ...comment,
        replies: repliesAdapter.addMany(
            repliesAdapter.getInitialState(),
            comment.replies,
        ),
    }));

    return (
        <Row>
            <Col xs={"auto"} lg={3} xxl={2}>
                <div className={"mb-3"}>
                    <VideoCard video={video} />
                </div>
            </Col>
            <Col xs={"auto"} lg={9} xxl={10}>
                <CommentList
                    comments={commentList}
                    highlightAuthorId={author.id}
                />
            </Col>
        </Row>
    );
};

export const VideoCommentsList = ({
    author,
    videos,
    comments,
}: VideoCommentsListProps) => {
    const videoCommentsList = [...videos]
        .sort(descBy((video) => DateTime.fromISO(video.publishedAt).valueOf()))
        .map((video) => ({
            video: video,
            comments: comments.filter(
                (comment) => comment.videoId === video.id,
            ),
        }));

    return (
        <>
            {videoCommentsList.map((videoComments) => (
                <VideoCommentsListItem
                    key={uuidv4()}
                    author={author}
                    {...videoComments}
                />
            ))}
        </>
    );
};
