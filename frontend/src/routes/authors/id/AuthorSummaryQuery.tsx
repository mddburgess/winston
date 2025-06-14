import { useGetAuthorQuery } from "#/api";
import type { GetAuthorResponse } from "#/api";
import type { ReactNode } from "react";

type Props = {
    authorHandle: string;
    children: {
        isLoading?: () => ReactNode;
        isSuccess: (summary: GetAuthorResponse) => ReactNode;
    };
};

export const AuthorSummaryQuery = ({ authorHandle, children }: Props) => {
    const { isLoading, isSuccess, data } = useGetAuthorQuery({
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
