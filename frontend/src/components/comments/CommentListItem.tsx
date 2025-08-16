import { Col, ListGroupItem, Row } from "react-bootstrap";
import { EyeSlash, EyeSlashFill, Flag, FlagFill } from "react-bootstrap-icons";
import { Link } from "react-router";
import { usePatchCommentPropertiesMutation } from "#/api";
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
    const [patchCommentProperties] = usePatchCommentPropertiesMutation();

    const highlight = highlightAuthorId === comment.author.id;
    const ImportantFlag = comment.properties.important ? FlagFill : Flag;
    const HiddenFlag = comment.properties.hidden ? EyeSlashFill : EyeSlash;
    const rowClass = comment.properties.important
        ? "bg-warning-subtle px-1 py-2 rounded text-warning-emphasis"
        : comment.properties.hidden
          ? "text-body-tertiary"
          : highlight
            ? "bg-info-subtle px-1 py-2 rounded text-info-emphasis"
            : "";
    const linkClass = comment.properties.hidden ? "text-body-tertiary" : "";

    const handleClickImportant = () => {
        void patchCommentProperties({
            id: comment.id,
            body: [
                {
                    op: "add",
                    path: "/important",
                    value: !comment.properties.important,
                },
                {
                    op: "add",
                    path: "/hidden",
                    value: false,
                },
            ],
        });
    };

    const handleClickHidden = () => {
        void patchCommentProperties({
            id: comment.id,
            body: [
                {
                    op: "add",
                    path: "/hidden",
                    value: !comment.properties.hidden,
                },
                {
                    op: "add",
                    path: "/important",
                    value: false,
                },
            ],
        });
    };

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
                    <ImportantFlag
                        className={"me-1"}
                        onClick={handleClickImportant}
                    />
                    <HiddenFlag
                        className={"ms-1"}
                        onClick={handleClickHidden}
                    />
                </Col>
                <Col xs={12}>
                    <HtmlText text={comment.text.display} />
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
