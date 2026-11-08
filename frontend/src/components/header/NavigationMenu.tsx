import { Dropdown, Nav, NavDropdown } from "react-bootstrap";
import { List } from "react-bootstrap-icons";
import { Link } from "react-router";
import { AvailableQuota } from "#/components/limits/AvailableQuota";
import { routes } from "#/utils/links";

const NavigationMenu = () => (
  <Nav>
    <NavDropdown align={"end"} title={<List className={"mb-1"} />}>
      <NavDropdown.Item as={Link} to={routes.home}>
        Channels
      </NavDropdown.Item>
      <NavDropdown.Item as={Link} to={routes.authors.list}>
        Authors
      </NavDropdown.Item>
      <NavDropdown.Item as={Link} to={routes.settings}>
        Settings
      </NavDropdown.Item>
      <NavDropdown.Divider />
      <Dropdown.Item disabled={true}>
        <AvailableQuota />
      </Dropdown.Item>
    </NavDropdown>
  </Nav>
);

export { NavigationMenu };
