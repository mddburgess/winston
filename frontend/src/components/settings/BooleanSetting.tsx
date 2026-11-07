import { Col, ListGroupItem, Row } from "react-bootstrap";
import { BooleanSettingToggle } from "./BooleanSettingToggle";
import type { Settings } from "#/api";
import type { Maybe } from "#/types";
import type { PickByValue } from "utility-types";

type BooleanSettingProps = {
  label: string;
  setting: keyof PickByValue<Settings, Maybe<boolean>>;
};

const BooleanSetting = ({ label, setting }: BooleanSettingProps) => (
  <ListGroupItem>
    <Row>
      <Col>{label}</Col>
      <Col xs={"auto"}>
        <BooleanSettingToggle setting={setting} />
      </Col>
    </Row>
  </ListGroupItem>
);

export { BooleanSetting };
