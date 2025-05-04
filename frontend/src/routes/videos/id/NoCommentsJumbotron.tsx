import { Button, Col, Row } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { useAppDispatch } from "../../../store/hooks";
import { requestedCommentsForVideoId } from "../../../store/slices/fetches";
import type { VideoWithChannelProps } from "../../../types";

export const NoCommentsJumbotron = ({ video }: VideoWithChannelProps) => {
    const dispatch = useAppDispatch();
    return (
        <Row className={"border border-dashed mx-0 my-3 p-5 rounded-3"}>
            <Col className={"text-center"}>
                <h2 className={"mb-4"}>
                    Comments haven&apos;t been fetched yet
                </h2>
                <Button
                    onClick={() =>
                        dispatch(requestedCommentsForVideoId(video.id))
                    }
                >
                    <span className={"align-items-center d-flex"}>
                        Fetch comments
                        <ArrowDownRightCircleFill className={"ms-2"} />
                    </span>
                </Button>
            </Col>
        </Row>
    );
};
