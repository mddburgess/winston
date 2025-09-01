import { Col, Row } from "react-bootstrap";
import type { ReactNode } from "react";
import type { Icon } from "react-bootstrap-icons";

type IconLabelProps = { icon: Icon } & (
  | { children: ReactNode; label?: never }
  | { label: string | number; children?: never }
);

const IconLabel = ({ icon: LabelIcon, ...props }: IconLabelProps) => (
  <Row className={"gx-2"}>
    <Col xs={"auto"} className={"flex-center"}>
      <LabelIcon />
    </Col>
    <Col xs={"auto"}>
      {props.children}
      {props.label}
    </Col>
  </Row>
);

export { IconLabel };
