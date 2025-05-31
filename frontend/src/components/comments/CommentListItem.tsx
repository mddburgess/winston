import { Col, ListGroupItem, Row } from "react-bootstrap";
import { Link } from "react-router";
import { Date } from "#/components/Date";
import { HtmlText } from "#/components/HtmlText";
import { selectAllReplies } from "#/store/slices/comments";
import { routes } from "#/utils/links";
import { ReplyList } from "./ReplyList";
import type { TopLevelComment } from "#/store/slices/backend";

type Props = {
    comment: TopLevelComment;
    highlightAuthorId?: string;
};

export const CommentListItem = ({ comment, highlightAuthorId = "" }: Props) => {
    const highlight = highlightAuthorId === comment.author.id;

    return (
        <ListGroupItem key={comment.id}>
            <Row>
                <Col xs={"auto"} className={"small"}>
                    <Link to={routes.authors.details(comment.author.handle)}>
                        {comment.author.handle}
                    </Link>
                </Col>
                <Col xs={"auto"} className={"ps-0 small"}>
                    <Date date={comment.published_at} />
                </Col>
            </Row>
            <Row>
                <Col
                    className={
                        highlight
                            ? "bg-info-subtle py-1 rounded text-info-emphasis"
                            : ""
                    }
                >
                    <HtmlText text={comment.text} />
                </Col>
            </Row>
            <Row>
                <ReplyList
                    commentId={comment.id}
                    totalReplyCount={comment.total_reply_count}
                    replies={selectAllReplies(comment.replies)}
                    highlightAuthorId={highlightAuthorId}
                />
            </Row>
        </ListGroupItem>
    );
};
