import { Col, Image, Row } from "react-bootstrap";
import { BadgeList } from "#/components/BadgeList";
import { CopyToClipboard } from "#/components/CopyToClipboard";
import type { Channel } from "#/api";

const topicTransformer = (topic: string) => {
    return topic
        .slice("https://en.wikipedia.org/wiki/".length)
        .replace(/_/g, " ");
};

type Props = {
    channel: Channel;
};

export const ChannelDetails = ({ channel }: Props) => (
    <Row className={"bg-body-tertiary border mx-0 my-3 p-3 rounded-3"}>
        <Col xs={"auto"}>
            <Image
                roundedCircle
                className={"border"}
                width={120}
                height={120}
                src={channel.thumbnail_url}
            />
        </Col>
        <Col>
            <p className={"h1"}>{channel.title}</p>
            <p className={"align-items-center d-flex h6"}>
                <a
                    href={`https://www.youtube.com/${channel.handle}`}
                    target={"_blank"}
                    rel={"noreferrer"}
                >
                    {channel.handle}
                </a>
                <CopyToClipboard
                    text={`https://www.youtube.com/${channel.handle}`}
                />
            </p>
            <p className={"small"}>{channel.description}</p>
            <Row xs={1} lg={2}>
                <Col className={"mb-3 mb-lg-0"}>
                    <p className={"h6"}>Topics</p>
                    <BadgeList
                        values={channel.topics}
                        transformer={topicTransformer}
                    />
                </Col>
                <Col>
                    <p className={"h6"}>Keywords</p>
                    <BadgeList values={channel.keywords} />
                </Col>
            </Row>
        </Col>
    </Row>
);
