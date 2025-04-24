import { Route, Routes } from "react-router";
import { ChannelsRoute } from "./channels";
import { ChannelsIdRoute } from "./channels/id";
import { AuthorsIdRoute } from "./authors/id";
import { VideosIdRoute } from "./videos/id";
import { AuthorsRoute } from "./authors";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<ChannelsRoute />} />
        <Route path={"/authors"} element={<AuthorsRoute />} />
        <Route path={"/authors/:authorId"} element={<AuthorsIdRoute />} />
        <Route path={"/channels/:channelId"} element={<ChannelsIdRoute />} />
        <Route path={"/videos/:videoId"} element={<VideosIdRoute />} />
    </Routes>
);
