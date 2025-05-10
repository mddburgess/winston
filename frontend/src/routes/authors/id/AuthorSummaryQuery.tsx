import { useFindAuthorSummaryByHandleQuery } from "../../../store/slices/authors";
import type { AuthorHandleProps, AuthorSummaryResponse } from "../../../types";
import type { ReactNode } from "react";

type AuthorSummaryQueryProps = AuthorHandleProps & {
    children: {
        isLoading?: () => ReactNode;
        isSuccess: (summary: AuthorSummaryResponse) => ReactNode;
    };
};

export const AuthorSummaryQuery = ({
    authorHandle,
    children,
}: AuthorSummaryQueryProps) => {
    const { isLoading, isSuccess, data } =
        useFindAuthorSummaryByHandleQuery(authorHandle);

    return isLoading && children.isLoading ? (
        children.isLoading()
    ) : isSuccess ? (
        children.isSuccess(data)
    ) : (
        <></>
    );
};
