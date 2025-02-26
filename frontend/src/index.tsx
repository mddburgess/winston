import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import {App} from "./App";

// @ts-ignore
createRoot(document.getElementById("root")).render(
    <StrictMode>
        <App/>
    </StrictMode>
);
