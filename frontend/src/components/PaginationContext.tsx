import { ReactNode, useMemo } from "react";
import { useSearchParams } from "react-router";
import { parseIntOrDefault } from "../utils";

type PaginationContextProps<T> = {
    pageSize: number;
    items: T[];
    children: (pageItems: PageInfo<T>) => ReactNode;
};

export type PageInfo<T> = {
    pageNumber: number;
    setPageNumber: (pageNumber: number) => void;
    pageSize: number;
    pageCount: number;
    pageItems: T[];
    totalItemCount: number;
};

export const PaginationContext = <T,>({
    pageSize,
    items,
    children,
}: PaginationContextProps<T>) => {
    const [searchParams, setSearchParams] = useSearchParams();
    const pageCount = Math.ceil(items.length / pageSize);
    const pageNumber = Math.max(
        1,
        Math.min(parseIntOrDefault(searchParams.get("p"), 1), pageCount),
    );
    const pageItems = useMemo(() => {
        const firstIndex = pageSize * (pageNumber - 1);
        const lastIndex = pageSize * pageNumber;
        return items.slice(firstIndex, lastIndex);
    }, [pageSize, items, pageNumber]);

    return children({
        pageNumber,
        setPageNumber: (nextPageNumber) =>
            setSearchParams({ p: `${nextPageNumber}` }),
        pageSize,
        pageCount: Math.ceil(items.length / pageSize),
        pageItems,
        totalItemCount: items.length,
    });
};
