import { Col, ListGroupItem, Row } from "react-bootstrap";
import { ReplyFill } from "react-bootstrap-icons";
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
        <ListGroupItem key={reply.id}>
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
            </Row>
            <Row>
                <Col
                    className={
                        highlight
                            ? "bg-info-subtle py-1 rounded text-info-emphasis"
                            : ""
                    }
                >
                    <HtmlText text={reply.text} />
                </Col>
            </Row>
        </ListGroupItem>
    );
};
