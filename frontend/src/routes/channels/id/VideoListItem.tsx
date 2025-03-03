import {VideoDto} from "../../../model/VideoDto";
import {Col, Image, ListGroupItem, Row} from "react-bootstrap";
import {Link} from "react-router";
import {ChatLeftQuoteFill, CloudDownload, CloudUpload, ReplyAllFill} from "react-bootstrap-icons";

type VideoListItemProps = {
    video: VideoDto
}

export const VideoListItem = ({ video }: VideoListItemProps) => (
    <ListGroupItem key={video.id}>
        <Row>
            <Col>
                <Image width={96} height={72} src={video.thumbnailUrl}/>
            </Col>
            <Col>
                <Link to={`/videos/${video.id}`}>
                    {video.title}
                </Link>
            </Col>
            <Col>
                {video.commentCount}
                <ChatLeftQuoteFill/>
            </Col>
            <Col>
                {video.replyCount}/{video.totalReplyCount}
                <ReplyAllFill/>
            </Col>
            <Col>
                {video.publishedAt} <CloudUpload/>
                <br/>
                {video.lastFetchedAt} <CloudDownload/>
            </Col>
        </Row>
    </ListGroupItem>
)
