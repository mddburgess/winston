import { Col, ListGroupItem, Row } from "react-bootstrap";
import {
    EyeSlash,
    EyeSlashFill,
    Flag,
    FlagFill,
    ReplyFill,
} from "react-bootstrap-icons";
import { Link } from "react-router";
import { usePatchCommentPropertiesMutation } from "#/api";
import { Date } from "#/components/Date";
import { HtmlText } from "#/components/HtmlText";
import { routes } from "#/utils/links";
import type { Comment } from "#/api";

type ReplyListItemProps = {
    reply: Comment;
    highlightAuthorId?: string;
};

export const ReplyListItem = ({
    reply,
    highlightAuthorId = "",
}: ReplyListItemProps) => {
    const [patchCommentProperties] = usePatchCommentPropertiesMutation();

    const highlight = highlightAuthorId === reply.author.id;
    const ImportantFlag = reply.properties.important ? FlagFill : Flag;
    const HiddenFlag = reply.properties.hidden ? EyeSlashFill : EyeSlash;
    const listGroupItemClass = reply.properties.important
        ? "bg-warning-subtle rounded text-warning-emphasis"
        : reply.properties.hidden
          ? "text-body-tertiary"
          : highlight
            ? "bg-info-subtle rounded text-info-emphasis"
            : "";
    const linkClass = reply.properties.hidden
        ? "small text-body-tertiary"
        : "small";

    const handleClickImportant = () => {
        void patchCommentProperties({
            id: reply.id,
            body: [
                {
                    op: "add",
                    path: "/important",
                    value: !reply.properties.important,
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
            id: reply.id,
            body: [
                {
                    op: "add",
                    path: "/hidden",
                    value: !reply.properties.hidden,
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
        <ListGroupItem key={reply.id} className={listGroupItemClass}>
            <Row>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <ReplyFill className={"me-2"} />
                    <Link
                        to={routes.authors.details(reply.author.handle)}
                        className={linkClass}
                    >
                        {reply.author.handle}
                    </Link>
                </Col>
                <Col xs={"auto"} className={"ps-0 small"}>
                    <Date date={reply.published_at} />
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
            </Row>
            <Row>
                <Col>
                    <HtmlText text={reply.text.display} />
                </Col>
            </Row>
        </ListGroupItem>
    );
};
