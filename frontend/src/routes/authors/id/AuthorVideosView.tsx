import { groupBy, sortBy, uniqBy } from "lodash";
import { Col, Row } from "react-bootstrap";
import { AuthorVideoCards } from "#/components/authors/AuthorVideoCards";
import type { AuthorProps, ChannelSummary, VideoListProps } from "#/types";

type AuthorVideosViewProps = AuthorProps & VideoListProps;

const AuthorVideosView = ({ author, videos }: AuthorVideosViewProps) => {
    const channels = sortBy(
        uniqBy(
            videos.map((video) => video.channel),
            "title",
        ),
        "title",
    );
    const channelVideos = groupBy(videos, (video) => video.channel.handle);

    return (
        <>
            {channels.map((channel) => (
                <AuthorChannel
                    key={channel.handle}
                    author={author}
                    channel={channel}
                    videos={channelVideos[channel.handle]}
                />
            ))}
        </>
    );
};

type AuthorChannelProps = AuthorProps &
    VideoListProps & {
        channel: ChannelSummary;
    };

const AuthorChannel = ({ author, channel, videos }: AuthorChannelProps) => (
    <Row>
        <Col xs={12}>
            <h2 className={"border-bottom"}>{channel.title}</h2>
        </Col>
        <Col xs={12}>
            <AuthorVideoCards author={author} videos={videos} />
        </Col>
    </Row>
);

export { AuthorVideosView };
