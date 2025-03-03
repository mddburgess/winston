import {Route, Routes} from "react-router";
import {ChannelsRoute} from "./channels";
import {ChannelVideos} from "./ChannelVideos";
import {Video} from "./Video";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<ChannelsRoute/>}/>
        <Route path={"/channels/:channelId"} element={<ChannelVideos/>}/>
        <Route path={"/videos/:videoId"} element={<Video/>}/>
    </Routes>
);
