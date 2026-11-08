import { Container } from "react-bootstrap";
import { Header } from "#/components/header/Header";
import { Footer } from "./Footer";
import { AppRoutes } from "./routes/AppRoutes";

export const App = () => (
  <>
    <Container>
      <Header />
      <AppRoutes />
    </Container>
    <Footer />
  </>
);
