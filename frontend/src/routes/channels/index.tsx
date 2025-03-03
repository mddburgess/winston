import {useListChannelsQuery} from "../../store/slices/api";
import {Container} from "react-bootstrap";
import {ChannelList} from "./ChannelList";

export const ChannelsRoute = () => {
    const { data: channels } = useListChannelsQuery()

    return (
        <Container>
            <h1>Channels</h1>
            <ChannelList channels={channels} />
        </Container>
    )
}
