import { OverlayTrigger, Tooltip } from "react-bootstrap";
import { Archive, ArchiveFill } from "react-bootstrap-icons";
import { IconButton } from "#/components/IconButton";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { toggleShowArchivedChannels } from "#/store/slices/preferences";

const ShowArchivedChannelsButton = () => {
  const dispatch = useAppDispatch();
  const showArchivedChannels = useAppSelector((state) => state.preferences.showArchivedChannels);

  const icon = showArchivedChannels ? ArchiveFill : Archive;
  const variant = showArchivedChannels ? "secondary" : "outline-secondary";

  const tooltipText = showArchivedChannels ? "Hide archived" : "Show archived";
  const tooltip = <Tooltip>{tooltipText}</Tooltip>;

  const handleClick = () => {
    dispatch(toggleShowArchivedChannels());
  };

  return (
    <OverlayTrigger overlay={tooltip}>
      <IconButton icon={icon} variant={variant} onClick={handleClick} />
    </OverlayTrigger>
  );
};

export { ShowArchivedChannelsButton };
