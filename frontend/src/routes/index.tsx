import {Route, Routes} from "react-router";
import {Channels} from "./Channels";
import {ChannelVideos} from "./ChannelVideos";
import {Video} from "./Video";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<Channels/>}/>
        <Route path={"/channels/:channelId"} element={<ChannelVideos/>}/>
        <Route path={"/videos/:videoId"} element={<Video/>}/>
    </Routes>
);
