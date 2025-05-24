import { Col, ListGroupItem, Row } from "react-bootstrap";
import { Link } from "react-router";
import { Date } from "#/components/Date";
import { HtmlText } from "#/components/HtmlText";
import { repliesAdapter } from "#/store/slices/comments";
import { routes } from "#/utils/links";
import { ReplyList } from "./ReplyList";
import type { CommentState } from "#/store/slices/comments";

type CommentListItemProps = {
    comment: CommentState;
    highlightAuthorId?: string;
};

export const CommentListItem = ({
    comment,
    highlightAuthorId = "",
}: CommentListItemProps) => {
    const highlight = highlightAuthorId === comment.author.id;

    return (
        <ListGroupItem key={comment.id}>
            <Row>
                <Col xs={"auto"} className={"small"}>
                    <Link to={routes.authors.details(comment.author)}>
                        {comment.author.displayName}
                    </Link>
                </Col>
                <Col xs={"auto"} className={"ps-0 small"}>
                    <Date date={comment.publishedAt} />
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
                    totalReplyCount={comment.totalReplyCount}
                    replies={repliesAdapter
                        .getSelectors()
                        .selectAll(comment.replies)}
                    highlightAuthorId={highlightAuthorId}
                />
            </Row>
        </ListGroupItem>
    );
};
