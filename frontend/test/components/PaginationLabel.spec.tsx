import { render } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { PaginationLabel } from "#/components/PaginationLabel";

describe(PaginationLabel, () => {
  it("displays zero when the item count is zero", () => {
    const result = render(
      <PaginationLabel itemLabel={"item"} itemCount={0} totalCount={5} pageSize={10} pageNumber={0} />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("0 items");
  });

  it("displays the item count when it equals the total count", () => {
    const result = render(
      <PaginationLabel itemLabel={"item"} itemCount={5} totalCount={5} pageSize={10} pageNumber={0} />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("5 items");
  });

  it("displays a singular noun when the item count is one", () => {
    const result = render(
      <PaginationLabel itemLabel={"item"} itemCount={1} totalCount={1} pageSize={10} pageNumber={0} />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("1 item");
  });

  it("displays the custom plural noun when it is provided", () => {
    const result = render(
      <PaginationLabel
        itemLabel={"reply"}
        pluralLabel={"replies"}
        itemCount={5}
        totalCount={5}
        pageSize={10}
        pageNumber={0}
      />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("5 replies");
  });

  it("displays a range when the item count is less than the total count", () => {
    const result = render(
      <PaginationLabel itemLabel={"item"} itemCount={5} totalCount={15} pageSize={10} pageNumber={1} />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("11 – 15 of 15 items");
  });

  it("displays the custom plural noun in ranges when it is provided", () => {
    const result = render(
      <PaginationLabel
        itemLabel={"reply"}
        pluralLabel={"replies"}
        itemCount={5}
        totalCount={15}
        pageSize={10}
        pageNumber={1}
      />,
    );
    const paginationLabel = result.getByTestId("paginationLabel");

    expect(paginationLabel).toHaveTextContent("11 – 15 of 15 replies");
  });
});
