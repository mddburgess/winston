import { Alert, Button, Col } from "react-bootstrap";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { batchPullComments } from "#/store/slices/pulls";
import { clearVideoSelection } from "#/store/slices/selections";
import { pluralize } from "#/utils";
import type { Video } from "#/api";
import type { ChannelProps } from "#/types";

type Props = ChannelProps & {
    videosOnPage: Video[];
};

const BatchPullCommentsAlert = ({ channel, videosOnPage }: Props) => {
    const videos = useAppSelector((state) => state.selections.videos);
    const dispatch = useAppDispatch();

    const handleClick = () => {
        const videosToPull = videos.length > 0 ? videos : videosOnPage;
        dispatch(
            batchPullComments({ channelId: channel.id, videos: videosToPull }),
        );
        dispatch(clearVideoSelection());
    };

    const label =
        videos.length > 0
            ? "Fetch comments for selected videos"
            : "Fetch comments for videos on page";

    return (
        <Alert className={"alert-primary align-items-center d-flex"}>
            <Col>
                <strong>{pluralize(videos.length, `video`)}</strong> selected.
            </Col>
            <Col xs={"auto"}>
                <Button onClick={handleClick}>{label}</Button>
            </Col>
        </Alert>
    );
};

export { BatchPullCommentsAlert };
