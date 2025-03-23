import {useListChannelsQuery} from "../../store/slices/api";
import {ChannelCards} from "./ChannelCards";
import {Button, Col, Row} from "react-bootstrap";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";
import {useState} from "react";
import {FetchChannelModal} from "./FetchChannelModal";

export const ChannelsRoute = () => {

    const { data: channels } = useListChannelsQuery()
    const [showModal, setShowModal] = useState(false);

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>
                        Channels
                    </p>
                </Col>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <Button
                        className={"align-items-center d-flex"}
                        onClick={() => setShowModal(true)}
                    >
                        Fetch...
                        <ArrowDownRightCircleFill className={"ms-2"}/>
                    </Button>
                </Col>
            </Row>
            <ChannelCards channels={channels} />
            <FetchChannelModal show={showModal} setShow={setShowModal} />
        </>
    )
}
