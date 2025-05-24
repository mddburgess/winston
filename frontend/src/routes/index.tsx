import { Route, Routes } from "react-router";
import { routes } from "#/utils/links";
import { AuthorListRoute } from "./authors";
import { AuthorDetailsRoute } from "./authors/id";
import { ChannelListRoute } from "./channels";
import { ChannelDetailsRoute } from "./channels/id";
import { VideoDetailsRoute } from "./videos/id";

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
