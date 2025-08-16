import { Col } from "react-bootstrap";
import { ChatFill, ChatQuoteFill, Youtube } from "react-bootstrap-icons";
import type { AuthorProps } from "#/types";

export const AuthorStatistics = ({ author }: AuthorProps) => (
    <>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"commentedVideos"}
            xs={"auto"}
        >
            <Youtube className={"me-2"} />
            {author.statistics?.video_count ?? 0}
        </Col>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"totalComments"}
            xs={"auto"}
        >
            <ChatFill className={"me-2"} />
            {author.statistics?.comment_count ?? 0}
        </Col>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"totalReplies"}
            xs={"auto"}
        >
            <ChatQuoteFill className={"me-2"} />
            {author.statistics?.reply_count ?? 0}
        </Col>
    </>
);
