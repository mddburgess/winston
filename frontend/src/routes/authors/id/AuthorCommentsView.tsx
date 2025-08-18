import { AuthorVideoNavigation } from "#/components/authors/AuthorVideoNavigation";
import { CommentList } from "#/components/comments/CommentList";
import { AuthorCommentsQuery } from "#/routes/authors/id/AuthorCommentsQuery";
import type { AuthorProps, VideoListProps, VideoProps } from "#/types";

type AuthorCommentsViewProps = AuthorProps &
    VideoListProps & {
        videoIndex: number;
    };

const AuthorCommentsView = ({
    author,
    videos,
    videoIndex,
}: AuthorCommentsViewProps) => {
    return (
        <>
            <AuthorVideoNavigation videos={videos} index={videoIndex} />
            <AuthorComments author={author} video={videos[videoIndex]} />
        </>
    );
};

type AuthorCommentsProps = AuthorProps & VideoProps;

const AuthorComments = ({ author, video }: AuthorCommentsProps) => (
    <AuthorCommentsQuery author={author} video={video}>
        {{
            isLoading: () => <small>Loading...</small>,
            isSuccess: (comments) => (
                <CommentList
                    comments={comments}
                    highlightAuthorId={author.id}
                />
            ),
        }}
    </AuthorCommentsQuery>
);

export { AuthorCommentsView };
