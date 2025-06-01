import { useGetAuthorByHandleQuery } from "#/api";
import type { GetAuthorByHandleResp } from "#/api";
import type { ReactNode } from "react";

type Props = {
    authorHandle: string;
    children: {
        isLoading?: () => ReactNode;
        isSuccess: (summary: GetAuthorByHandleResp) => ReactNode;
    };
};

export const AuthorSummaryQuery = ({ authorHandle, children }: Props) => {
    const { isLoading, isSuccess, data } = useGetAuthorByHandleQuery({
        handle: authorHandle,
    });

    return isLoading && children.isLoading ? (
        children.isLoading()
    ) : isSuccess ? (
        children.isSuccess(data)
    ) : (
        <></>
    );
};
