import { Col, Row } from "react-bootstrap";
import { ArrowUpLeftCircleFill } from "react-bootstrap-icons";
import { Date } from "#/components/Date";
import type { VideoProps } from "#/types";

const VideoPublishedAt = ({ video }: VideoProps) => (
    <Row className={"g-0"}>
        <Col xs={"auto"} className={"flex-center"}>
            <ArrowUpLeftCircleFill className={"me-2"} />
        </Col>
        <Col xs={"auto"}>
            <Date date={video.published_at} />
        </Col>
    </Row>
);

export { VideoPublishedAt };
