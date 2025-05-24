import { Container } from "react-bootstrap";
import { Footer } from "./Footer";
import { Header } from "./Header";
import { AppRoutes } from "./routes";

export const App = () => (
    <>
        <Container>
            <Header />
            <AppRoutes />
        </Container>
        <Footer />
    </>
);
