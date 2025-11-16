import { useGetSettingsQuery } from "#/api";
import type { ReactNode } from "react";

const IfAuthorFocus = (props: { children: (authorHandle: string) => ReactNode }) => {
  const { isSuccess, data } = useGetSettingsQuery();

  if (!isSuccess || data.author_focus === undefined) {
    return undefined;
  }
  return props.children(data.author_focus);
};

export { IfAuthorFocus };
