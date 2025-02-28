import {Route, Routes} from "react-router";
import {Channels} from "./Channels";

export const AppRoutes = () => (
    <Routes>
        <Route path={"/"} element={<Channels/>}/>
    </Routes>
);
