import { Col, Row } from "react-bootstrap";
import {
    EyeSlash,
    EyeSlashFill,
    Flag,
    FlagFill,
    ReplyFill,
} from "react-bootstrap-icons";
import { usePatchCommentPropertiesMutation } from "#/api";
import { AuthorLink } from "#/components/authors/AuthorLink";
import { AuthorProfileImage } from "#/components/authors/AuthorProfileImage";
import { Date } from "#/components/Date";
import { HtmlText } from "#/components/HtmlText";
import type { Comment } from "#/api";
import type { CommentState } from "#/store/slices/backend";

type CommentProps = {
    comment: CommentState | Comment;
};

type CommentDisplayRowProps = CommentProps & {
    highlightAuthorId?: string;
    isReply?: boolean;
};

const CommentDisplayRow = ({
    comment,
    highlightAuthorId = "",
    isReply,
}: CommentDisplayRowProps) => {
    const highlight = highlightAuthorId === comment.author.id;
    const rowClass = comment.properties.important
        ? "bg-warning-subtle text-warning-emphasis"
        : comment.properties.hidden
          ? "text-body-tertiary"
          : highlight
            ? "bg-info-subtle text-info-emphasis"
            : "";
    const linkClass = comment.properties.hidden
        ? "small text-body-tertiary"
        : "small";

    return (
        <>
            <Row className={`gx-2 ${rowClass}`}>
                {isReply && (
                    <Col xs={"auto"} className={"flex-center"}>
                        <ReplyFill />
                    </Col>
                )}
                <Col xs={"auto"}>
                    <AuthorProfileImage
                        author={comment.author}
                        minWidth={"1.5rem"}
                    />
                </Col>
                <Col xs={"auto"} className={"flex-center"}>
                    <AuthorLink author={comment.author} className={linkClass} />
                </Col>
                <Col xs={"auto"} className={"flex-center small"}>
                    <Date date={comment.published_at} />
                </Col>
                <Col className={"flex-center justify-content-end"}>
                    <ToggleImportantLink comment={comment} />
                    <ToggleHiddenLink comment={comment} />
                </Col>
            </Row>
            <Row className={`gx-2 pt-1 ${rowClass}`}>
                <Col>
                    <HtmlText text={comment.text.display} />
                </Col>
            </Row>
        </>
    );
};

const ToggleImportantLink = ({ comment }: CommentProps) => {
    const [patchCommentProperties] = usePatchCommentPropertiesMutation();
    const handleClick = () => {
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
    const Icon = comment.properties.important ? FlagFill : Flag;

    return <Icon className={"me-2"} onClick={handleClick} />;
};

const ToggleHiddenLink = ({ comment }: CommentProps) => {
    const [patchCommentProperties] = usePatchCommentPropertiesMutation();
    const handleClick = () => {
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
    const Icon = comment.properties.hidden ? EyeSlashFill : EyeSlash;

    return <Icon className={"me-0"} onClick={handleClick} />;
};

export { CommentDisplayRow };
