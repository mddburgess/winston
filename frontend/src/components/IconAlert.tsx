import { Alert, Col, Row } from "react-bootstrap";
import type { Maybe } from "#/types";
import type { AlertProps } from "react-bootstrap";
import type { Icon } from "react-bootstrap-icons";

type IconAlertProps = AlertProps & {
  icon: Icon;
  label: Maybe<string>;
};

const IconAlert = ({ icon: AlertIcon, label, ...alertProps }: IconAlertProps) => (
  <Row className={"mt-3"}>
    <Col>
      <Alert {...alertProps} className={`m-0 py-2 ${alertProps.className}`}>
        <Row className={"g-3"}>
          <Col xs={"auto"} className={"flex-center"}>
            <AlertIcon />
          </Col>
          <Col>{label}</Col>
        </Row>
      </Alert>
    </Col>
  </Row>
);

export { IconAlert };
