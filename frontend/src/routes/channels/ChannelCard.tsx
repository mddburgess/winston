import { ChannelDto } from "../../model/ChannelDto";
import { Card, Col, Image, Row } from "react-bootstrap";
import { Link } from "react-router";
import {
    ArrowDownRightCircle,
    ArrowUpLeftCircleFill,
    Youtube,
} from "react-bootstrap-icons";
import { Date } from "../../components/Date";

type ChannelCardProps = {
    channel: ChannelDto;
};

export const ChannelCard = ({ channel }: ChannelCardProps) => (
    <Col className={"g-2"}>
        <Card className={"h-100"}>
            <Card.Body className={"p-2"}>
                <Row className={"m-0"}>
                    <Col className={"col-2 col-md-3 p-0"}>
                        <Image
                            roundedCircle
                            className={"border w-100"}
                            src={channel.thumbnailUrl}
                        />
                    </Col>
                    <Col className={"col-10 col-md-9"}>
                        <p className={"fs-5 mb-1"}>
                            <Link to={`/channels/${channel.id}`}>
                                {channel.title}
                            </Link>
                        </p>
                        <p className={"mb-1 small line-clamp"}>
                            {channel.description}
                        </p>
                    </Col>
                </Row>
            </Card.Body>
            <Card.Footer>
                <Row>
                    <Col className={"align-items-center col-auto d-flex"}>
                        <Youtube className={"me-2"} />
                        {channel.videoCount}
                    </Col>
                </Row>
            </Card.Footer>
            <Card.Footer>
                <Row>
                    <Col className={"align-items-center col-auto d-flex"}>
                        <ArrowUpLeftCircleFill className={"me-2"} />
                        <Date date={channel.publishedAt} />
                    </Col>
                    <Col className={"align-items-center d-flex"}>
                        <ArrowDownRightCircle className={"me-2"} />
                        <Date date={channel.lastFetchedAt} />
                    </Col>
                </Row>
            </Card.Footer>
        </Card>
    </Col>
);
