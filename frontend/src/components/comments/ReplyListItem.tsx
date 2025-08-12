import { Col, ListGroupItem, Row } from "react-bootstrap";
import { EyeSlash, Flag, ReplyFill } from "react-bootstrap-icons";
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

    return (
        <ListGroupItem
            key={reply.id}
            className={
                highlight ? "bg-info-subtle rounded text-info-emphasis" : ""
            }
        >
            <Row>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <ReplyFill className={"me-2"} />
                    <Link
                        to={routes.authors.details(reply.author.handle)}
                        className={"small"}
                    >
                        {reply.author.handle}
                    </Link>
                </Col>
                <Col xs={"auto"} className={"ps-0 small"}>
                    <Date date={reply.published_at} />
                </Col>
                <Col></Col>
                <Col xs={"auto"} className={"pe-2 d-flex align-items-center"}>
                    <Flag className={"me-1"} />
                    <EyeSlash className={"ms-1"} />
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
