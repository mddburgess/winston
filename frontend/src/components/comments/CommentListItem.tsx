import {Col, ListGroupItem, Row} from "react-bootstrap";
import {ReplyList} from "./ReplyList";
import {Link} from "react-router";
import {CommentDto} from "../../model/CommentDto";
import {Date} from "../Date";
import {HtmlText} from "../HtmlText";

type CommentListItemProps = {
    comment: CommentDto,
    highlightAuthorId?: string
}

export const CommentListItem = ({ comment, highlightAuthorId = "" }: CommentListItemProps) => {
    const highlight = highlightAuthorId === comment.author.id;

    return (
        <ListGroupItem key={comment.id}>
            <Row>
                <Col xs={"auto"} className={"small"}>
                    <Link to={`/authors/${comment.author.id}`}>
                        {comment.author.displayName}
                    </Link>
                </Col>
                <Col xs={"auto"} className={"ps-0 small"}>
                    <Date date={comment.publishedAt}/>
                </Col>
            </Row>
            <Row>
                <Col className={highlight ? "bg-info-subtle py-1 rounded text-info-emphasis" : ""}>
                    <HtmlText text={comment.text}/>
                </Col>
            </Row>
            <Row>
                <ReplyList
                    totalReplyCount={comment.totalReplyCount}
                    replies={comment.replies}
                    highlightAuthorId={highlightAuthorId}
                />
            </Row>
        </ListGroupItem>
    );
}
