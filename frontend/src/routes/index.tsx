import {Route, Routes} from "react-router";
import {Channels} from "./Channels";
import {ChannelVideos} from "./ChannelVideos";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<Channels/>}/>
        <Route path={"/channels/:channelId"} element={<ChannelVideos/>}/>
    </Routes>
);
