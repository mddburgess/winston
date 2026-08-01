import { Col, Row } from "react-bootstrap";
import type { ReactNode } from "react";
import type { Icon } from "react-bootstrap-icons";

type IconLabelProps = { icon: Icon; reverse?: boolean } & (
  { children: ReactNode; label?: never } | { label?: string | number; children?: never }
);

const IconLabel = ({ icon: LabelIcon, reverse = false, ...props }: IconLabelProps) => (
  <Row className={`${reverse ? "flex-row-reverse" : ""} flex-nowrap gx-2`} data-testid={"row"}>
    <Col xs={"auto"} className={"flex-center"}>
      <LabelIcon data-testid={"icon"} />
    </Col>
    {(props.children || props.label) && (
      <Col xs={"auto"} data-testid={"label"}>
        {props.children}
        {props.label}
      </Col>
    )}
  </Row>
);

export { IconLabel };
