import { Alert, Col } from "react-bootstrap";
import { ArrowDownRightCircleFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppSelector } from "#/store/hooks";
import { pluralize } from "#/utils";

type BatchPullCommentsAlertProps = {
  onClick: () => void;
};

const BatchPullCommentsAlert = ({ onClick }: BatchPullCommentsAlertProps) => {
  const selectedVideos = useAppSelector((state) => state.selections.videos);

  const label = selectedVideos.length > 0 ? "Pull comments for selected videos" : "Pull comments for videos on page";

  return (
    <Alert className={"alert-primary flex-center"}>
      <Col>
        <strong>{pluralize(selectedVideos.length, `video`)}</strong> selected.
      </Col>
      <Col xs={"auto"}>
        <IconButton icon={ArrowDownRightCircleFill} label={label} onClick={onClick} />
      </Col>
    </Alert>
  );
};

export { BatchPullCommentsAlert };
