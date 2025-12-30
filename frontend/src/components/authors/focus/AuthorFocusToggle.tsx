import { PersonBoundingBox } from "react-bootstrap-icons";
import { useGetSettingsQuery, usePatchSettingsMutation } from "#/api";
import { IconButton } from "#/components/IconButton";
import type { PatchOperation } from "#/api";
import type { AuthorProps } from "#/types";

const AuthorFocusToggle = (props: AuthorProps) => {
  const { isSuccess, data } = useGetSettingsQuery();
  const [patchSettings] = usePatchSettingsMutation();

  const hasFocus = isSuccess && data.author_focus === props.author.handle;
  const variant = hasFocus ? "info" : "outline-secondary";

  const handleClick = () => {
    const patchAuthorFocus: PatchOperation = hasFocus
      ? { op: "remove", path: "author_focus" }
      : { op: "add", path: "author_focus", value: props.author.handle };
    void patchSettings({ body: [patchAuthorFocus] });
  };

  return <IconButton icon={PersonBoundingBox} variant={variant} onClick={handleClick} />;
};

export { AuthorFocusToggle };
