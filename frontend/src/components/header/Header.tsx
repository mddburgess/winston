import { EyeFill } from "react-bootstrap-icons";
import { NavigationMenu } from "#/components/header/NavigationMenu";
import { routes } from "#/utils/links";

export const Header = () => (
  <header className={"d-flex flex-wrap justify-content-center py-3 mb-3 border-bottom"}>
    <a href={routes.home} className={"d-flex align-items-center mb-0 me-auto link-body-emphasis text-decoration-none"}>
      <EyeFill className={"fs-4 me-2"} />
      <span className={"fs-4"}>Winston</span>
    </a>
    <NavigationMenu />
  </header>
);
