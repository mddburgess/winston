import {Route, Routes} from "react-router";
import {ChannelsRoute} from "./channels";
import {Video} from "./Video";
import {ChannelsIdRoute} from "./channels/id";
import {AuthorsIdRoute} from "./authors/id";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<ChannelsRoute/>}/>
        <Route path={"/authors/:authorId"} element={<AuthorsIdRoute/>}/>
        <Route path={"/channels/:channelId"} element={<ChannelsIdRoute/>}/>
        <Route path={"/videos/:videoId"} element={<Video/>}/>
    </Routes>
);
