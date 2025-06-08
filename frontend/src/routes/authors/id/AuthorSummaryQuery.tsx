import { useGetAuthorByHandleQuery } from "#/api";
import type { GetAuthorByHandleResponse } from "#/api";
import type { ReactNode } from "react";

type Props = {
    authorHandle: string;
    children: {
        isLoading?: () => ReactNode;
        isSuccess: (summary: GetAuthorByHandleResponse) => ReactNode;
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
