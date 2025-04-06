import {EyeFill} from "react-bootstrap-icons";
import {Nav, Navbar, NavItem, Row} from "react-bootstrap";
import {Link} from "react-router";

export const Header = () => (
    <header className={"d-flex flex-wrap justify-content-center py-3 mb-3 border-bottom"}>
        <a href={"/"}
           className={"d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none"}>
            <EyeFill className={"fs-4 me-2"}/>
            <span className={"fs-4"}>Winston</span>
        </a>
        <Nav variant={"pills"}>
            <Nav.Item>
                <Nav.Link as={Link} to={"/"}>
                    Channels
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to={"/authors"}>
                    Authors
                </Nav.Link>
            </Nav.Item>
        </Nav>
    </header>
);
