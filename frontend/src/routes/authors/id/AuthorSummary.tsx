import { groupBy, sortedUniqBy } from "lodash";
import { useContext } from "react";
import {
    Accordion,
    AccordionBody,
    AccordionContext,
    AccordionHeader,
    AccordionItem,
    Col,
    Row,
} from "react-bootstrap";
import { AuthorVideoCards } from "#/components/authors/AuthorVideoCards";
import { CommentList } from "#/components/comments/CommentList";
import { AuthorCommentsQuery } from "./AuthorCommentsQuery";
import type { GetAuthorResponse } from "#/api";
import type {
    AuthorProps,
    ChannelSummary,
    VideoListProps,
    VideoProps,
} from "#/types";

type AuthorChannelSummaryProps = {
    channel: ChannelSummary;
} & AuthorProps &
    VideoListProps;

type AccordionProps = AuthorProps & VideoProps;

const AccordionContent = ({ author, video }: AccordionProps) => {
    const { activeEventKey } = useContext(AccordionContext);
    const isCurrentEventKey = activeEventKey === video.id;
    return isCurrentEventKey ? (
        <AuthorCommentsQuery author={author} video={video}>
            {{
                isLoading: () => <small>Loading...</small>,
                isSuccess: (comments) => (
                    <CommentList
                        comments={comments}
                        highlightAuthorId={author.id}
                    />
                ),
            }}
        </AuthorCommentsQuery>
    ) : (
        <></>
    );
};

const AuthorChannelSummary = ({
    author,
    channel,
    videos,
}: AuthorChannelSummaryProps) => (
    <>
        <h2 className={"border-bottom"}>{channel.title}</h2>
        <AuthorVideoCards author={author} videos={videos} />
        <Accordion>
            {videos.map((video) => (
                <AccordionItem key={video.id} eventKey={video.id}>
                    <AccordionHeader>{video.title}</AccordionHeader>
                    <AccordionBody>
                        <AccordionContent author={author} video={video} />
                    </AccordionBody>
                </AccordionItem>
            ))}
        </Accordion>
    </>
);

export const AuthorSummary = ({ author, videos }: GetAuthorResponse) => {
    const channels = sortedUniqBy(
        videos.map((video) => video.channel),
        (channel) => channel.title,
    );
    const channelVideos = groupBy(videos, (video) => video.channel.handle);
    return (
        <Row>
            <Col>
                {channels.map((channel) => (
                    <AuthorChannelSummary
                        key={channel.handle}
                        author={author}
                        channel={channel}
                        videos={channelVideos[channel.handle]}
                    />
                ))}
            </Col>
        </Row>
    );
};
