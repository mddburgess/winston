import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { toggleSelectVideo } from "#/store/slices/selections";
import type { Video } from "#/api";
import type { ChangeEvent } from "react";

type Props = {
    video: Video;
};

const VideoCardSelectionCheckbox = ({ video }: Props) => {
    const dispatch = useAppDispatch();
    const selected = useAppSelector(
        (state) => state.selections.videos[video.id] ?? false,
    );

    const onChange = (event: ChangeEvent<HTMLInputElement>) => {
        dispatch(toggleSelectVideo(video.id));
        console.log(`clicked ${video.id}`);
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
