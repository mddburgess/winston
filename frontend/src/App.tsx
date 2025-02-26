import {Route, Routes} from "react-router";

export const App = () => (
    <Routes>
        <Route path={"/"} element={<Channels/>}/>
        <Route path={"/videos"} element={<Videos/>}/>
        <Route path={"/comments"} element={<Comments/>}/>
    </Routes>
);

const Channels = () => (
    <h1>Channels</h1>
);

const Videos = () => (
    <h1>Videos</h1>
);

const Comments = () => (
    <h1>Comments</h1>
);
