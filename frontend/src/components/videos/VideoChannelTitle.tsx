import { Col, Row } from "react-bootstrap";
import { PersonVideo3 } from "react-bootstrap-icons";
import type { VideoProps } from "#/types";

const VideoChannelTitle = ({ video }: VideoProps) => (
    <Row className={"g-0"}>
        <Col xs={"auto"} className={"flex-center"}>
            <PersonVideo3 className={"me-2"} />
        </Col>
        <Col xs={"auto"}>{video.channel.title}</Col>
    </Row>
);

export { VideoChannelTitle };
