import {ChannelDto} from "../../../model/ChannelDto";
import {Col, Image, Row} from "react-bootstrap";
import {BadgeList} from "../../../components/BadgeList";
import {CopyToClipboard} from "../../../components/CopyToClipboard";

type ChannelDetailsProps = {
    channel: ChannelDto
}

export const ChannelDetails = ({channel}: ChannelDetailsProps) => (
    <Row className={"bg-body-tertiary border mx-0 my-3 p-3 rounded-3"}>
        <Col xs={"auto"}>
            <Image
                roundedCircle
                className={"border"}
                width={120} height={120}
                src={channel.thumbnailUrl}
            />
        </Col>
        <Col>
            <p className={"h1"}>
                {channel.title}
            </p>
            <p className={"align-items-center d-flex h6"}>
                <a href={`https://www.youtube.com/${channel.customUrl}`} target={"_blank"}>
                    {channel.customUrl}
                </a>
                <CopyToClipboard text={`https://www.youtube.com/${channel.customUrl}`}/>
            </p>
            <p className={"small"}>
                {channel.description}
            </p>
            <Row xs={1} lg={2}>
                <Col className={"mb-3 mb-lg-0"}>
                    <p className={"h6"}>
                        Topics
                    </p>
                    <BadgeList values={channel.topics} transformer={topicTransformer}/>
                </Col>
                <Col>
                    <p className={"h6"}>
                        Keywords
                    </p>
                    <BadgeList values={channel.keywords}/>
                </Col>
            </Row>
        </Col>
    </Row>
)

const topicTransformer = (topic: string) => {
    return topic.slice("https://en.wikipedia.org/wiki/".length).replace(/_/g, " ");
}
