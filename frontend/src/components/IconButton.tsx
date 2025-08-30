import { Button } from "react-bootstrap";
import type { ButtonProps } from "react-bootstrap";
import type { Icon } from "react-bootstrap-icons";

type IconButtonProps = ButtonProps & {
  icon: Icon;
  label: string;
};

const IconButton = ({ icon: ButtonIcon, label, ...buttonProps }: IconButtonProps) => (
  <Button {...buttonProps}>
    <span className={"flex-center"}>
      {label} <ButtonIcon className={"ms-2"} />
    </span>
  </Button>
);

export { IconButton };
