import {EyeFill} from "react-bootstrap-icons";

export const Header = () => (
    <header className={"d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom"}>
        <a href={"/"} className={"d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none"}>
            <EyeFill className={"fs-4 me-2"}/>
            <span className={"fs-4"}>Winston</span>
        </a>
    </header>
);
