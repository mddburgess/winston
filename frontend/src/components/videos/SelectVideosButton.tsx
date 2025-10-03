import { OverlayTrigger, Tooltip } from "react-bootstrap";
import { HandIndexThumbFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { toggleVideoSelectionActive } from "#/store/slices/selections";

const SelectVideosButton = () => {
  const dispatch = useAppDispatch();
  const selections = useAppSelector((state) => state.selections);

  const label = selections.active ? selections.videos.length : undefined;
  const variant = selections.active ? "info" : "outline-secondary";

  const handleClick = () => {
    dispatch(toggleVideoSelectionActive());
  };

  return (
    <OverlayTrigger overlay={<Tooltip>Select videos</Tooltip>}>
      <IconButton icon={HandIndexThumbFill} label={label} variant={variant} onClick={handleClick} />
    </OverlayTrigger>
  );
};

export { SelectVideosButton };
