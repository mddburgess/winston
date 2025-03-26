import {useListChannelsQuery} from "../../store/slices/api";
import {ChannelCards} from "./ChannelCards";
import {Button, Col, Row} from "react-bootstrap";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";
import {useMemo, useState} from "react";
import {FetchChannelModal} from "./FetchChannelModal";
import {PaginationRow} from "../../components/PaginationRow";

export const ChannelsRoute = () => {

    const { data: channels } = useListChannelsQuery()
    const [showModal, setShowModal] = useState(false);

    const pageSize = 12;
    const [page, setPage] = useState(1);
    const displayedChannels = useMemo(
        () => channels?.slice(pageSize * (page - 1), pageSize * page) ?? [],
        [channels, pageSize, page]
    );

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
            <PaginationRow
                name={"channel"}
                total={channels?.length ?? 0}
                pageSize={pageSize}
                page={page}
                setPage={setPage}
            />
            <ChannelCards channels={displayedChannels} />
            <FetchChannelModal show={showModal} setShow={setShowModal} />
        </>
    )
}
