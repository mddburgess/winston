import { describe, expect, it } from "vitest";
import { PaginationControl } from "#/components/PaginationControl";
import { renderWithRouter } from "=/utils/render";

describe(PaginationControl, () => {
  const paginationControl = <PaginationControl totalCount={50} pageSize={10} />;

  it("is not displayed when the total count is 0", () => {
    const result = renderWithRouter(<PaginationControl totalCount={0} pageSize={10} />);
    const buttons = result.queryAllByRole("button");

    expect(buttons).toHaveLength(0);
  });

  it("is not displayed when the total count is equal to the page size", () => {
    const result = renderWithRouter(<PaginationControl totalCount={10} pageSize={10} />);
    const buttons = result.queryAllByRole("button");

    expect(buttons).toHaveLength(0);
  });

  it("is displayed when the total count is greater than the page size", () => {
    const result = renderWithRouter(paginationControl, { route: "?i=20" });
    const buttons = result.getAllByRole("button");

    expect(buttons).toHaveLength(4);
  });

  describe("first page button", () => {
    it("is disabled when the current page is the first page", () => {
      const result = renderWithRouter(paginationControl);
      const firstPageButton = result.getByTestId("firstPageButton");

      expect(firstPageButton).not.toHaveRole("button");
    });

    it("is enabled when the current page is greater than the first page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const firstPageButton = result.getByTestId("firstPageButton");

      expect(firstPageButton).toHaveRole("button");
    });

    it("navigates to the first page when clicked", async () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const firstPageButton = result.getByTestId("firstPageButton");

      await result.user.click(firstPageButton);

      expect(window.location.search).toBe("?i=0");
    });
  });

  describe("previous page button", () => {
    it("is disabled when the current page is the first page", () => {
      const result = renderWithRouter(paginationControl);
      const prevPageButton = result.getByTestId("prevPageButton");

      expect(prevPageButton).not.toHaveRole("button");
    });

    it("is enabled when the current page is greater than the first page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const prevPageButton = result.getByTestId("prevPageButton");

      expect(prevPageButton).toHaveRole("button");
    });

    it("navigates to the previous page when clicked", async () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const prevPageButton = result.getByTestId("prevPageButton");

      await result.user.click(prevPageButton);

      expect(window.location.search).toBe("?i=10");
    });
  });

  describe("next page button", () => {
    it("is disabled when the current page is the last page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=40" });
      const nextPageButton = result.getByTestId("nextPageButton");

      expect(nextPageButton).not.toHaveRole("button");
    });

    it("is enabled when the current page is less than the last page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const nextPageButton = result.getByTestId("nextPageButton");

      expect(nextPageButton).toHaveRole("button");
    });

    it("navigates to the next page when clicked", async () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const nextPageButton = result.getByTestId("nextPageButton");

      await result.user.click(nextPageButton);

      expect(window.location.search).toBe("?i=30");
    });
  });

  describe("last page button", () => {
    it("is disabled when the current page is the last page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=40" });
      const lastPageButton = result.getByTestId("lastPageButton");

      expect(lastPageButton).not.toHaveRole("button");
    });

    it("is enabled when the current page is less than the last page", () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const lastPageButton = result.getByTestId("lastPageButton");

      expect(lastPageButton).toHaveRole("button");
    });

    it("navigates to the last page when clicked", async () => {
      const result = renderWithRouter(paginationControl, { route: "?i=20" });
      const lastPageButton = result.getByTestId("lastPageButton");

      await result.user.click(lastPageButton);

      expect(window.location.search).toBe("?i=40");
    });
  });
});
