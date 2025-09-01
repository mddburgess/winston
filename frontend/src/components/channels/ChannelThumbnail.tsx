import { Image } from "react-bootstrap";
import type { ChannelProps } from "#/types";

const ChannelThumbnail = ({ channel }: ChannelProps) => (
  <Image roundedCircle className={"border"} width={120} height={120} src={channel.thumbnail_url} />
);

export { ChannelThumbnail };
