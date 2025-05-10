import { Col, ListGroupItem, Row } from "react-bootstrap";
import { ReplyList } from "./ReplyList";
import { Link } from "react-router";
import { type CommentState, repliesAdapter } from "../../store/slices/comments";
import { Date } from "../Date";
import { HtmlText } from "../HtmlText";
import { routes } from "../../utils/links";

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
