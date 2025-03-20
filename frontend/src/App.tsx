import {AppRoutes} from "./routes";
import {Header} from "./Header";
import {Footer} from "./Footer";
import {Container} from "react-bootstrap";

export const App = () => (
    <>
        <Container>
            <Header/>
            <AppRoutes/>
        </Container>
        <Footer/>
    </>
);
