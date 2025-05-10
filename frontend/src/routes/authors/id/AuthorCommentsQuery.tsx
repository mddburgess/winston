import {
    commentsAdapter,
    useListCommentsByVideoIdAuthorQuery,
} from "../../../store/slices/comments";
import type { CommentState } from "../../../store/slices/comments";
import type { AuthorHandleProps, VideoProps } from "../../../types";
import type { ReactNode } from "react";

type AuthorCommentsQueryProps = VideoProps &
    AuthorHandleProps & {
        children: {
            isLoading?: () => ReactNode;
            isSuccess: (summary: CommentState[]) => ReactNode;
        };
    };

export const AuthorCommentsQuery = ({
    video,
    authorHandle,
    children,
}: AuthorCommentsQueryProps) => {
    const { isLoading, isSuccess, data } = useListCommentsByVideoIdAuthorQuery({
        videoId: video.id,
        authorHandle,
    });
    return isLoading && children.isLoading ? (
        children.isLoading()
    ) : isSuccess ? (
        children.isSuccess(commentsAdapter.getSelectors().selectAll(data))
    ) : (
        <></>
    );
};
