import { Button } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { requestedRepliesForId } from "#/store/slices/fetches";
import { FetchVideoRepliesAction } from "./FetchVideoRepliesAction";
import type { VideoProps } from "#/types";

const FetchVideoRepliesButton = ({ video }: VideoProps) => {
    const fetchState = useAppSelector(
        (state) => state.fetches.replies[video.id],
    );

    if (
        fetchState?.status === "REQUESTED" ||
        fetchState?.status === "FETCHING"
    ) {
        return <FetchVideoRepliesAction id={video.id} />;
    } else {
        return <FVRButton video={video} />;
    }
};

const FVRButton = ({ video }: VideoProps) => {
    const dispatch = useAppDispatch();
    return (
        <Button
            className={"align-items-center d-flex"}
            onClick={() => dispatch(requestedRepliesForId(video.id))}
            size={"sm"}
        >
            <ArrowDownRightCircleFill />
        </Button>
    );
};

export { FetchVideoRepliesButton };
