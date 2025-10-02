import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { describe, expect, it } from "vitest";
import { PaginationControl } from "#/components/PaginationControl";

describe(PaginationControl, () => {
  it("is not displayed when the total count is 0", () => {
    const result = render(
      <MemoryRouter>
        <PaginationControl totalCount={0} pageSize={10} pageNumber={0} />
      </MemoryRouter>,
    );
    const buttons = result.queryAllByRole("button");

    expect(buttons).toHaveLength(0);
  });

  it("is not displayed when the total count is equal to the page size", () => {
    const result = render(
      <MemoryRouter>
        <PaginationControl totalCount={10} pageSize={10} pageNumber={0} />
      </MemoryRouter>,
    );
    const buttons = result.queryAllByRole("button");

    expect(buttons).toHaveLength(0);
  });

  it("is displayed when the total count is greater than the page size", () => {
    const result = render(
      <MemoryRouter>
        <PaginationControl totalCount={30} pageSize={10} pageNumber={1} />
      </MemoryRouter>,
    );
    const buttons = result.getAllByRole("button");

    expect(buttons).toHaveLength(4);
  });
});
