import { useGetAuthorQuery } from "#/api";
import type { GetAuthorResp } from "#/api";
import type { ReactNode } from "react";

type Props = {
  authorHandle: string;
  children: {
    isLoading?: () => ReactNode;
    isSuccess: (summary: GetAuthorResp) => ReactNode;
  };
};

export const AuthorSummaryQuery = ({ authorHandle, children }: Props) => {
  const { isLoading, isSuccess, data } = useGetAuthorQuery({
    handle: authorHandle,
  });

  return isLoading && children.isLoading ? children.isLoading() : isSuccess ? children.isSuccess(data) : <></>;
};
