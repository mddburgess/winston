import { findIndex } from "lodash";
import { useSearchParams } from "react-router";
import { AuthorCommentsView } from "#/routes/authors/id/AuthorCommentsView";
import { AuthorVideosView } from "#/routes/authors/id/AuthorVideosView";
import type { AuthorProps, VideoListProps } from "#/types";

type AuthorDetailsViewProps = AuthorProps & VideoListProps;

const AuthorDetailsView = ({ author, videos }: AuthorDetailsViewProps) => {
    const [searchParams] = useSearchParams();
    const videoId = searchParams.get("v");
    const videoIndex = findIndex(videos, (v) => v.id === videoId);

    return videoIndex > -1 ? (
        <AuthorCommentsView
            author={author}
            videos={videos}
            videoIndex={videoIndex}
        />
    ) : (
        <AuthorVideosView author={author} videos={videos} />
    );
};

export { AuthorDetailsView };
