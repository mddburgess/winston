import { ListGroup } from "react-bootstrap";
import { BooleanSetting } from "#/components/settings/BooleanSetting";

const SettingsList = () => (
  <ListGroup>
    <BooleanSetting label={"Hide video thumbnails"} />
    <BooleanSetting label={"Hide video titles"} />
  </ListGroup>
);

export { SettingsList };
