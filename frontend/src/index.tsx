import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import {BrowserRouter} from "react-router";
import {App} from "./App";
import "bootstrap/dist/css/bootstrap.min.css"

const root = document.getElementById("root")!;
createRoot(root).render(
    <StrictMode>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </StrictMode>
);
