import {Col, Image, ListGroupItem, Row} from "react-bootstrap";
import {CloudDownload, CloudUpload, Youtube} from "react-bootstrap-icons";
import {Link} from "react-router";
import {ChannelDto} from "../../model/ChannelDto";

type ChannelListItemProps = {
    channel: ChannelDto
}

export const ChannelListItem = ({channel}: ChannelListItemProps) => (
    <ListGroupItem key={channel.id}>
        <Row>
            <Col>
                <Image width={64} height={64} src={channel.thumbnailUrl}/>
            </Col>
            <Col>
                <h5>
                    <Link to={`/channels/${channel.id}`}>
                        {channel.title}
                    </Link>
                </h5>
                <h6>{channel.customUrl}</h6>
            </Col>
            <Col>
                {channel.videoCount}
                <Youtube/>
            </Col>
            <Col>
                {channel.publishedAt}
                <CloudUpload/>
            </Col>
            <Col>
                {channel.lastFetchedAt}
                <CloudDownload/>
            </Col>
        </Row>
    </ListGroupItem>
)
