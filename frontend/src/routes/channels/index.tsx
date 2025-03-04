import {useListChannelsQuery} from "../../store/slices/api";
import {Breadcrumb, BreadcrumbItem, Container} from "react-bootstrap";
import {ChannelList} from "./ChannelList";

export const ChannelsRoute = () => {
    const { data: channels } = useListChannelsQuery()

    return (
        <Container>
            <Breadcrumb>
                <BreadcrumbItem active>
                    Channels
                </BreadcrumbItem>
            </Breadcrumb>
            <h1>Channels</h1>
            <ChannelList channels={channels} />
        </Container>
    )
}
