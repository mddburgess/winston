import { Archive, ArchiveFill } from "react-bootstrap-icons";
import { usePatchChannelPropertiesMutation } from "#/api";
import type { ChannelProps } from "#/types";

const ArchiveChannelButton = ({ channel }: ChannelProps) => {
  const [patchChannelProperties] = usePatchChannelPropertiesMutation();

  const archived = channel.properties?.archived ?? false;
  const ArchiveIcon = archived ? ArchiveFill : Archive;

  const handleClick = () => {
    void patchChannelProperties({
      handle: channel.handle,
      body: [{ op: "add", path: "/archived", value: !archived }],
    });
  };

  return <ArchiveIcon onClick={handleClick} />;
};

export { ArchiveChannelButton };
