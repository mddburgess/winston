import {useListChannelsQuery} from "../../store/slices/api";
import {ChannelCards} from "./ChannelCards";

export const ChannelsRoute = () => {
    const { data: channels } = useListChannelsQuery()

    return (
        <>
            <h1 className={"mb-2"}>Channels</h1>
            <ChannelCards channels={channels} />
        </>
    )
}
