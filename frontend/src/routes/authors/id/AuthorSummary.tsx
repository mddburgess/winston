import { groupBy } from "lodash";
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
import { CommentList } from "#/components/comments/CommentList";
import { AuthorCommentsQuery } from "./AuthorCommentsQuery";
import type {
    AuthorHandleProps,
    ChannelListProps,
    ChannelProps,
    VideoListProps,
    VideoProps,
} from "#/types";

type AuthorChannelSummaryProps = ChannelProps &
    VideoListProps &
    AuthorHandleProps;
type AuthorSummaryProps = ChannelListProps & VideoListProps & AuthorHandleProps;

type Props = VideoProps & AuthorHandleProps;

const AccordionContent = ({ video, authorHandle }: Props) => {
    const { activeEventKey } = useContext(AccordionContext);
    const isCurrentEventKey = activeEventKey === video.id;
    return isCurrentEventKey ? (
        <AuthorCommentsQuery video={video} authorHandle={authorHandle}>
            {{
                isLoading: () => <small>Loading...</small>,
                isSuccess: (comments) => <CommentList comments={comments} />,
            }}
        </AuthorCommentsQuery>
    ) : (
        <></>
    );
};

const AuthorChannelSummary = ({
    channel,
    videos,
    authorHandle,
}: AuthorChannelSummaryProps) => (
    <>
        <h4>{channel.title}</h4>
        <Accordion>
            {videos.map((video) => (
                <AccordionItem key={video.id} eventKey={video.id}>
                    <AccordionHeader>{video.title}</AccordionHeader>
                    <AccordionBody>
                        <AccordionContent
                            video={video}
                            authorHandle={authorHandle}
                        />
                    </AccordionBody>
                </AccordionItem>
            ))}
        </Accordion>
    </>
);

export const AuthorSummary = ({
    channels,
    videos,
    authorHandle,
}: AuthorSummaryProps) => {
    const channelVideos = groupBy(videos, (video) => video.channelId);
    return (
        <Row>
            <Col>
                {channels.map((channel) => (
                    <AuthorChannelSummary
                        key={channel.id}
                        channel={channel}
                        videos={channelVideos[channel.id]}
                        authorHandle={authorHandle}
                    />
                ))}
            </Col>
        </Row>
    );
};
