import { Button, Col, Row } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { useAppDispatch } from "#/store/hooks";
import { requestedCommentsForVideoId } from "#/store/slices/fetches";
import type { VideoProps } from "#/types";

export const NoCommentsJumbotron = ({ video }: VideoProps) => {
    const dispatch = useAppDispatch();
    const hasBeenFetched = video.comments?.last_fetched_at !== undefined;
    return (
        <Row className={"border border-dashed mx-0 my-3 p-5 rounded-3"}>
            <Col className={"text-center"}>
                <h2 className={"mb-0"}>
                    {hasBeenFetched
                        ? `This video has no comments`
                        : `Comments haven't been fetched yet`}
                </h2>
                {!hasBeenFetched && (
                    <Button
                        className={"mt-4"}
                        onClick={() =>
                            dispatch(requestedCommentsForVideoId(video.id))
                        }
                    >
                        <span className={"align-items-center d-flex"}>
                            Fetch comments
                            <ArrowDownRightCircleFill className={"ms-2"} />
                        </span>
                    </Button>
                )}
            </Col>
        </Row>
    );
};
