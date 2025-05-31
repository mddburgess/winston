import {
    selectAllTopLevelComments,
    useListCommentsForVideoQuery,
} from "#/store/slices/comments";
import type { TopLevelComment } from "#/store/slices/backend";
import type { AuthorHandleProps, VideoProps } from "#/types";
import type { ReactNode } from "react";

type AuthorCommentsQueryProps = VideoProps &
    AuthorHandleProps & {
        children: {
            isLoading?: () => ReactNode;
            isSuccess: (summary: TopLevelComment[]) => ReactNode;
        };
    };

export const AuthorCommentsQuery = ({
    video,
    authorHandle,
    children,
}: AuthorCommentsQueryProps) => {
    const { isLoading, isSuccess, data } = useListCommentsForVideoQuery({
        id: video.id,
        author: authorHandle,
    });
    return isLoading && children.isLoading ? (
        children.isLoading()
    ) : isSuccess ? (
        children.isSuccess(selectAllTopLevelComments(data))
    ) : (
        <></>
    );
};
