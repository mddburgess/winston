import { PersonVideo3 } from "react-bootstrap-icons";
import { IconLabel } from "#/components/IconLabel";
import type { VideoProps } from "#/types";

const VideoChannelTitle = ({ video }: VideoProps) => <IconLabel icon={PersonVideo3} label={video.channel.title} />;

export { VideoChannelTitle };
