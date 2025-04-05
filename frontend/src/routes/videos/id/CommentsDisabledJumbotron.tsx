import {Col, Row} from "react-bootstrap";

export const CommentsDisabledJumbotron = () => (
    <Row className={"bg-secondary-subtle border border-secondary mx-0 my-3 p-5 rounded-3 text-secondary-emphasis"}>
        <Col>
            <h2 className={"text-center"}>
                Comments are disabled for this video
            </h2>
        </Col>
    </Row>
)
