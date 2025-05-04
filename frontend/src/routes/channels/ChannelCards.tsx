import { Row } from "react-bootstrap";
import type { ChannelListProps } from "../../types";
import { ChannelCard } from "./ChannelCard";

export const ChannelCards = ({ channels }: ChannelListProps) => (
    <Row xs={1} md={2} lg={3} xxl={4} className={"px-2"}>
        {channels.map((channel) => (
            <ChannelCard key={channel.id} channel={channel} />
        ))}
    </Row>
);
