import { Row } from "react-bootstrap";
import { ChannelCard } from "./ChannelCard";
import type { Channel } from "#/api";

type Props = {
    channels: Channel[];
};

export const ChannelCards = ({ channels }: Props) => (
    <Row xs={1} md={2} lg={3} xxl={4} className={"px-2"}>
        {channels.map((channel) => (
            <ChannelCard key={channel.id} channel={channel} />
        ))}
    </Row>
);
