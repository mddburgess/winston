import { Form } from "react-bootstrap";
import { useGetSettingsQuery, usePatchSettingsMutation } from "#/api";
import type { Settings } from "#/api";
import type { Maybe } from "#/types";
import type { PickByValue } from "utility-types";

type BooleanSettingToggleProps = {
  setting: keyof PickByValue<Settings, Maybe<boolean>>;
};

const BooleanSettingToggle = ({ setting }: BooleanSettingToggleProps) => {
  const { isSuccess, data } = useGetSettingsQuery();
  const [patchSettings] = usePatchSettingsMutation();

  const value = isSuccess && data[setting] === true;

  const handleClick = () => {
    void patchSettings({ body: [{ op: "add", path: setting, value: !value }] });
  };

  return <Form.Switch checked={value} disabled={!isSuccess} onClick={handleClick} />;
};

export { BooleanSettingToggle };
