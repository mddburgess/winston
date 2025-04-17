import {selectAllChannels, useListChannelsQuery} from "../../store/slices/channels";
import {ChannelCards} from "./ChannelCards";
import {Button, Col, Row} from "react-bootstrap";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";
import {useMemo, useState} from "react";
import {FetchChannelModal} from "./FetchChannelModal";
import {PaginationRow} from "../../components/PaginationRow";
import {useSearchParams} from "react-router";

export const ChannelsRoute = () => {

    const [searchParams, setSearchParams] = useSearchParams()

    const { isSuccess, data } = useListChannelsQuery()
    const channels = isSuccess ? selectAllChannels(data) : [];

    const [showModal, setShowModal] = useState(false);

    const pageSize = 12;
    const displayedChannels = useMemo(
        () => {
            const page = parseInt(searchParams.get("p") ?? "1")
            return channels.slice(pageSize * (page - 1), pageSize * page) ?? []
        },
        [channels, pageSize, searchParams]
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
                total={channels.length}
                pageSize={pageSize}
                page={parseInt(searchParams.get("p") ?? "1")}
                setPage={(page) => setSearchParams({ p: `${page}` })}
            />
            <ChannelCards channels={displayedChannels} />
            <FetchChannelModal show={showModal} setShow={setShowModal} />
        </>
    )
}
