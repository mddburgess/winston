import { Dropdown, Nav, NavDropdown } from "react-bootstrap";
import { EyeFill, List } from "react-bootstrap-icons";
import { Link } from "react-router";
import { RemainingQuota } from "./RemainingQuota";
import { routes } from "./utils/links";

export const Header = () => (
    <header
        className={
            "d-flex flex-wrap justify-content-center py-3 mb-3 border-bottom"
        }
    >
        <a
            href={routes.home}
            className={
                "d-flex align-items-center mb-0 me-auto link-body-emphasis text-decoration-none"
            }
        >
            <EyeFill className={"fs-4 me-2"} />
            <span className={"fs-4"}>Winston</span>
        </a>
        <Nav>
            <NavDropdown align={"end"} title={<List className={"mb-1"} />}>
                <NavDropdown.Item as={Link} to={routes.home}>
                    Channels
                </NavDropdown.Item>
                <NavDropdown.Item as={Link} to={routes.authors.list}>
                    Authors
                </NavDropdown.Item>
                <NavDropdown.Divider />
                <Dropdown.Item disabled={true}>
                    <div className={"small text-body-tertiary"}>
                        Remaining quota
                    </div>
                    <div className={"text-body-tertiary fw-bold"}>
                        <RemainingQuota />
                    </div>
                </Dropdown.Item>
            </NavDropdown>
        </Nav>
    </header>
);
