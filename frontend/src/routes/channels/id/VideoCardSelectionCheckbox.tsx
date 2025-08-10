import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { toggleSelectVideo } from "#/store/slices/selections";
import type { Video } from "#/api";

type Props = {
    video: Video;
};

const VideoCardSelectionCheckbox = ({ video }: Props) => {
    const dispatch = useAppDispatch();
    const selected = useAppSelector((state) =>
        state.selections.videos.includes(video),
    );

    const onChange = () => {
        dispatch(toggleSelectVideo(video));
    };

    return (
        <input
            type={"checkbox"}
            className={"video-card-checkbox"}
            checked={selected}
            onChange={onChange}
        />
    );
};

export { VideoCardSelectionCheckbox };
