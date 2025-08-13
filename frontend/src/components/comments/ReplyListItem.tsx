import { Col, ListGroupItem, Row } from "react-bootstrap";
import {
    EyeSlash,
    EyeSlashFill,
    Flag,
    FlagFill,
    ReplyFill,
} from "react-bootstrap-icons";
import { Link } from "react-router";
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
    const highlight = highlightAuthorId === reply.author.id;
    const ImportantFlag = reply.important ? FlagFill : Flag;
    const HiddenFlag = reply.hidden ? EyeSlashFill : EyeSlash;
    const listGroupItemClass = reply.important
        ? "bg-warning-subtle rounded text-warning-emphasis"
        : reply.hidden
          ? "text-body-tertiary"
          : highlight
            ? "bg-info-subtle rounded text-info-emphasis"
            : "";
    const linkClass = reply.hidden ? "small text-body-tertiary" : "small";

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
                    <ImportantFlag className={"me-1"} />
                    <HiddenFlag className={"ms-1"} />
                </Col>
            </Row>
            <Row>
                <Col>
                    <HtmlText text={reply.text} />
                </Col>
            </Row>
        </ListGroupItem>
    );
};
