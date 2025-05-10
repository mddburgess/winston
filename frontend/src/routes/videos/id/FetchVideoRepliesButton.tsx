import { Button } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { useAppDispatch, useAppSelector } from "../../../store/hooks";
import { requestedRepliesForId } from "../../../store/slices/fetches";
import { FetchVideoRepliesAction } from "./FetchVideoRepliesAction";

type FVRProps = {
    videoId: string;
};

export const FetchVideoRepliesButton = ({ videoId }: FVRProps) => {
    const fetchState = useAppSelector(
        (state) => state.fetches.replies[videoId],
    );

    if (
        fetchState?.status === "REQUESTED" ||
        fetchState?.status === "FETCHING"
    ) {
        return <FetchVideoRepliesAction videoId={videoId} />;
    } else {
        return <FVRButton videoId={videoId} />;
    }
};

const FVRButton = ({ videoId }: FVRProps) => {
    const dispatch = useAppDispatch();
    return (
        <Button
            className={"align-items-center d-flex"}
            onClick={() => dispatch(requestedRepliesForId(videoId))}
            size={"sm"}
        >
            <ArrowDownRightCircleFill />
        </Button>
    );
};
