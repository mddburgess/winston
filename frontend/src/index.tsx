import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import {Provider} from "react-redux";
import {BrowserRouter} from "react-router";
import {App} from "./App";
import {store} from "./store"
import "bootstrap/dist/css/bootstrap.min.css"
import "./index.css"

const root = document.getElementById("root")!;
createRoot(root).render(
    <StrictMode>
        <Provider store={store}>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
        </Provider>
    </StrictMode>
);
