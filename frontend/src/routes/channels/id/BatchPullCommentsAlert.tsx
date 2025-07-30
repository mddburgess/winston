import { filter } from "lodash";
import { Alert, Button, Col } from "react-bootstrap";
import { useAppSelector } from "#/store/hooks";
import { pluralize } from "#/utils";

const BatchPullCommentsAlert = () => {
    const videos = useAppSelector((state) => state.selections.videos);
    const selectedVideos = filter(videos, (value) => value).length;

    return (
        <Alert className={"alert-primary align-items-center d-flex"}>
            <Col>
                <strong>{pluralize(selectedVideos, `video`)}</strong> selected.
            </Col>
            <Col xs={"auto"}>
                <Button>Fetch comments...</Button>
            </Col>
        </Alert>
    );
};

export { BatchPullCommentsAlert };
