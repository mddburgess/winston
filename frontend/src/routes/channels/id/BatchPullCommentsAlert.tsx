import { Alert, Button, Col } from "react-bootstrap";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { requestPullComments } from "#/store/slices/pullVideoComments";
import { clearVideoSelection } from "#/store/slices/selections";
import { pluralize } from "#/utils";
import type { VideoListProps } from "#/types";

const BatchPullCommentsAlert = ({ videos }: VideoListProps) => {
    const dispatch = useAppDispatch();

    const selectedVideos = useAppSelector((state) => state.selections.videos);

    const handleClick = () => {
        const videosToPull =
            selectedVideos.length > 0 ? selectedVideos : videos;
        dispatch(requestPullComments(videosToPull));
        dispatch(clearVideoSelection());
    };

    const label =
        selectedVideos.length > 0
            ? "Fetch comments for selected videos"
            : "Fetch comments for videos on page";

    return (
        <Alert className={"alert-primary align-items-center d-flex"}>
            <Col>
                <strong>{pluralize(selectedVideos.length, `video`)}</strong>{" "}
                selected.
            </Col>
            <Col xs={"auto"}>
                <Button onClick={handleClick}>{label}</Button>
            </Col>
        </Alert>
    );
};

export { BatchPullCommentsAlert };
