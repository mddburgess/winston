import { Col } from "react-bootstrap";
import {
    ChatFill,
    ChatQuoteFill,
    PersonVideo3,
    Youtube,
} from "react-bootstrap-icons";
import type { AuthorProps } from "#/types";

export const AuthorStatistics = ({ author }: AuthorProps) => (
    <>
        <Col
            className={"flex-center"}
            data-testid={"commentedChannels"}
            xs={"auto"}
        >
            <PersonVideo3 className={"me-2"} />
            {author.author_statistics?.channel_count ?? 0}
        </Col>
        <Col
            className={"flex-center"}
            data-testid={"commentedVideos"}
            xs={"auto"}
        >
            <Youtube className={"me-2"} />
            {author.author_statistics?.video_count ?? 0}
        </Col>
        <Col
            className={"flex-center"}
            data-testid={"totalComments"}
            xs={"auto"}
        >
            <ChatFill className={"me-2"} />
            {author.author_statistics?.comment_count ?? 0}
        </Col>
        <Col className={"flex-center"} data-testid={"totalReplies"} xs={"auto"}>
            <ChatQuoteFill className={"me-2"} />
            {author.author_statistics?.reply_count ?? 0}
        </Col>
    </>
);
