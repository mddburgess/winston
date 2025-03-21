import {useListChannelsQuery} from "../../store/slices/api";
import {ChannelCards} from "./ChannelCards";
import {Button, Col, Row, Stack} from "react-bootstrap";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";

export const ChannelsRoute = () => {
    const { data: channels } = useListChannelsQuery()

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>
                        Channels
                    </p>
                </Col>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <Button className={"align-items-center d-flex"}>
                        Fetch...
                        <ArrowDownRightCircleFill className={"ms-2"}/>
                    </Button>
                </Col>
            </Row>
            <ChannelCards channels={channels} />
        </>
    )
}
