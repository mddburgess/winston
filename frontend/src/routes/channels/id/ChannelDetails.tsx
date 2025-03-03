import {ChannelDto} from "../../../model/ChannelDto";
import {Col, Image, Row} from "react-bootstrap";

type ChannelDetailsProps = {
    channel: ChannelDto
}

export const ChannelDetails = ({channel}: ChannelDetailsProps) => (
    <Row>
        <Col>
            <Image width={200} height={200} src={channel.thumbnailUrl}/>
        </Col>
        <Col>
            <h2>{channel.title}</h2>
            <h3>{channel.customUrl}</h3>
        </Col>
    </Row>
)
