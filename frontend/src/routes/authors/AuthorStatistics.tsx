import { Col } from "react-bootstrap";
import { ChatFill, ChatQuoteFill, Youtube } from "react-bootstrap-icons";
import type { AuthorWithStatistics } from "#/types";

type AuthorStatisticsProps = {
    author: AuthorWithStatistics;
};

export const AuthorStatistics = ({ author }: AuthorStatisticsProps) => (
    <>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"commentedVideos"}
            xs={"auto"}
        >
            <Youtube className={"me-2"} />
            {author.statistics.commentedVideos}
        </Col>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"totalComments"}
            xs={"auto"}
        >
            <ChatFill className={"me-2"} />
            {author.statistics.totalComments}
        </Col>
        <Col
            className={"align-items-center d-flex"}
            data-testid={"totalReplies"}
            xs={"auto"}
        >
            <ChatQuoteFill className={"me-2"} />
            {author.statistics.totalReplies}
        </Col>
    </>
);
