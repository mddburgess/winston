import { useState } from "react";
import { Button, Col, Row } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { PaginationContext } from "#/components/PaginationContext";
import { PaginationRow } from "#/components/PaginationRow";
import {
    selectAllChannels,
    useListChannelsQuery,
} from "#/store/slices/channels";
import { ChannelCards } from "./ChannelCards";
import { FetchChannelModal } from "./FetchChannelModal";

export const ChannelListRoute = () => {
    const { isSuccess, data } = useListChannelsQuery();
    const channels = isSuccess ? selectAllChannels(data) : [];
    const [showModal, setShowModal] = useState(false);

    return (
        <>
            <Row className={"mb-2"}>
                <Col className={"align-items-center d-flex"}>
                    <p className={"h1 m-0"}>Channels</p>
                </Col>
                <Col xs={"auto"} className={"align-items-center d-flex"}>
                    <Button
                        className={"align-items-center d-flex"}
                        onClick={() => setShowModal(true)}
                    >
                        Fetch...
                        <ArrowDownRightCircleFill className={"ms-2"} />
                    </Button>
                </Col>
            </Row>
            <PaginationContext pageSize={12} items={channels}>
                {({
                    pageNumber,
                    setPageNumber,
                    pageSize,
                    pageItems,
                    totalItemCount,
                }) => (
                    <>
                        <PaginationRow
                            name={"channel"}
                            total={totalItemCount}
                            pageSize={pageSize}
                            page={pageNumber}
                            setPage={setPageNumber}
                        />
                        <ChannelCards channels={pageItems} />
                    </>
                )}
            </PaginationContext>
            <FetchChannelModal show={showModal} setShow={setShowModal} />
        </>
    );
};
