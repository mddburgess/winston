import { Button } from "react-bootstrap";
import { IconLabel } from "#/components/IconLabel";
import type { ButtonProps } from "react-bootstrap";
import type { Icon } from "react-bootstrap-icons";

type IconButtonProps = ButtonProps & {
  icon: Icon;
  label?: string | number;
};

const IconButton = ({ icon, label, ...buttonProps }: IconButtonProps) => (
  <Button {...buttonProps}>
    <IconLabel icon={icon} label={label} reverse />
  </Button>
);

export { IconButton };
