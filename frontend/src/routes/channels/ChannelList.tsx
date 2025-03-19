import {ListGroup} from "react-bootstrap";
import {ChannelDto} from "../../model/ChannelDto";
import {ChannelListItem} from "./ChannelListItem";

type ChannelListProps = {
    channels?: ChannelDto[]
}

export const ChannelList = ({channels = []}: ChannelListProps) => (
    <ListGroup>
        {channels.map((channel) => (<ChannelListItem key={channel.id} channel={channel}/>))}
    </ListGroup>
)
