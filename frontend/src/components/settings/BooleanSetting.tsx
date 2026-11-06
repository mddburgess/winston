import { Col, Form, ListGroupItem, Row } from "react-bootstrap";

type BooleanSettingProps = {
  label: string;
};

const BooleanSetting = ({ label }: BooleanSettingProps) => (
  <ListGroupItem>
    <Row>
      <Col>{label}</Col>
      <Col xs={"auto"}>
        <Form.Switch />
      </Col>
    </Row>
  </ListGroupItem>
);

export { BooleanSetting };
