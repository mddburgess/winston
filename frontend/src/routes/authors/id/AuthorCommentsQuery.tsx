import {
    selectAllTopLevelComments,
    useListCommentsForVideoQuery,
} from "#/store/slices/comments";
import type { Author, Video } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";
import type { ReactNode } from "react";

type Props = {
    author: Author;
    video: Video;
    children: {
        isLoading?: () => ReactNode;
        isSuccess: (summary: TopLevelComment[]) => ReactNode;
    };
};

export const AuthorCommentsQuery = ({ author, video, children }: Props) => {
    const { isLoading, isSuccess, data } = useListCommentsForVideoQuery({
        id: video.id,
        author: author.handle,
    });

    return isLoading && children.isLoading ? (
        children.isLoading()
    ) : isSuccess ? (
        children.isSuccess(selectAllTopLevelComments(data))
    ) : (
        <></>
    );
};
