import { ListGroupItem } from "react-bootstrap";
import { ReplyAll } from "react-bootstrap-icons";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { pullRepliesRequested } from "#/store/slices/pullReplies";
import { pluralize } from "#/utils";
import type { Comment } from "#/api";

const PullRepliesActionItem = (props: { commentId: string; totalReplyCount: number; replies: Comment[] }) => {
  const dispatch = useAppDispatch();
  const { active, requested, responses } = useAppSelector((state) => state.pullReplies);

  if (props.replies.length >= props.totalReplyCount) {
    return undefined;
  }

  const isPulling = (active && requested === props.commentId) || responses[props.commentId];

  const handleClick = () => {
    if (!active) {
      dispatch(pullRepliesRequested({ commentId: props.commentId }));
    }
  };

  return (
    <ListGroupItem className={"flex-center"}>
      <ReplyAll className={"me-2"} />
      {isPulling ? (
        <span className={"small"}>
          Pulling {pluralize(props.totalReplyCount - props.replies.length, "more reply...", "more replies...")}
        </span>
      ) : (
        <a className={"small"} onClick={handleClick}>
          {pluralize(props.totalReplyCount - props.replies.length, "more reply...", "more replies...")}
        </a>
      )}
    </ListGroupItem>
  );
};

export { PullRepliesActionItem };
