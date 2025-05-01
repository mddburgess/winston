import { Route, Routes } from "react-router";
import { ChannelListRoute } from "./channels";
import { ChannelDetailsRoute } from "./channels/id";
import { AuthorDetailsRoute } from "./authors/id";
import { VideoDetailsRoute } from "./videos/id";
import { AuthorListRoute } from "./authors";
import { routes } from "../utils/links";

export const AppRoutes = () => (
    <Routes>
        <Route path={routes.home} element={<ChannelListRoute />} />
        <Route path={routes.authors.list} element={<AuthorListRoute />} />
        <Route
            path={routes.authors.details()}
            element={<AuthorDetailsRoute />}
        />
        <Route
            path={routes.channels.details()}
            element={<ChannelDetailsRoute />}
        />
        <Route path={routes.videos.details()} element={<VideoDetailsRoute />} />
    </Routes>
);
