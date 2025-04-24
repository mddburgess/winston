import { Row } from "react-bootstrap";
import { ChannelDto } from "../../model/ChannelDto";
import { ChannelCard } from "./ChannelCard";

type ChannelCardsProps = {
    channels?: ChannelDto[];
};

export const ChannelCards = ({ channels = [] }: ChannelCardsProps) => (
    <Row xs={1} md={2} lg={3} xxl={4} className={"px-2"}>
        {channels.map((channel) => (
            <ChannelCard key={channel.id} channel={channel} />
        ))}
    </Row>
);
