import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { PaginationContext } from "./PaginationContext";

describe("PaginationContext", () => {
    const items = ["first", "second", "third"];

    it("renders all items when the total count is less than the page size", () => {
        const pageInfoFn = vi.fn();
        const paginationContext = (
            <MemoryRouter>
                <PaginationContext pageSize={10} items={items}>
                    {pageInfoFn}
                </PaginationContext>
            </MemoryRouter>
        );
        render(paginationContext);
        expect(pageInfoFn).toHaveBeenCalledWith(
            expect.objectContaining({
                pageNumber: 1,
                pageSize: 10,
                pageCount: 1,
                pageItems: items,
                totalItemCount: 3,
            }),
        );
    });

    it("renders a page when the total count is greater than the page size", () => {
        const pageInfoFn = vi.fn();
        const paginationContext = (
            <MemoryRouter>
                <PaginationContext pageSize={2} items={items}>
                    {pageInfoFn}
                </PaginationContext>
            </MemoryRouter>
        );
        render(paginationContext);
        expect(pageInfoFn).toHaveBeenCalledWith(
            expect.objectContaining({
                pageNumber: 1,
                pageSize: 2,
                pageCount: 2,
                pageItems: ["first", "second"],
                totalItemCount: 3,
            }),
        );
    });
});
