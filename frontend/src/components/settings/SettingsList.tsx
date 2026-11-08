import { ListGroup } from "react-bootstrap";
import { BooleanSetting } from "#/components/settings/BooleanSetting";

const SettingsList = () => (
  <ListGroup>
    <BooleanSetting label={"Hide video thumbnails"} setting={"hide_video_thumbnails"} />
    <BooleanSetting label={"Hide video titles"} setting={"hide_video_titles"} />
  </ListGroup>
);

export { SettingsList };
