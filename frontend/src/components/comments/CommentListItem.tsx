import { Col, ListGroupItem, Row } from "react-bootstrap";
import { EyeSlash, EyeSlashFill, Flag, FlagFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { Date } from "#/components/Date";
import { HtmlText } from "#/components/HtmlText";
import { selectAllReplies } from "#/store/slices/comments";
import { routes } from "#/utils/links";
import { ReplyList } from "./ReplyList";
import type { CommentState } from "#/store/slices/backend";

type Props = {
    comment: CommentState;
    highlightAuthorId?: string;
};

export const CommentListItem = ({ comment, highlightAuthorId = "" }: Props) => {
    const highlight = highlightAuthorId === comment.author.id;
    const ImportantFlag = comment.important ? FlagFill : Flag;
    const HiddenFlag = comment.hidden ? EyeSlashFill : EyeSlash;
    const rowClass = comment.important
        ? "bg-warning-subtle px-1 py-2 rounded text-warning-emphasis"
        : comment.hidden
          ? "text-body-tertiary"
          : highlight
            ? "bg-info-subtle px-1 py-2 rounded text-info-emphasis"
            : "";
    const linkClass = comment.hidden ? "text-body-tertiary" : "";

    return (
        <ListGroupItem key={comment.id}>
            <Row className={rowClass}>
                <Col xs={"auto"} className={"small"}>
                    <Link
                        to={routes.authors.details(comment.author.handle)}
                        className={linkClass}
                    >
                        {comment.author.handle}
                    </Link>
                </Col>
                <Col className={"ps-0 small"}>
                    <Date date={comment.published_at} />
                </Col>
                <Col></Col>
                <Col xs={"auto"} className={"pe-2 d-flex align-items-center"}>
                    <ImportantFlag className={"me-1"} />
                    <HiddenFlag className={"ms-1"} />
                </Col>
                <Col xs={12}>
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
