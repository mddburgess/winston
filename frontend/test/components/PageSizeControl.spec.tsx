import { render } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import { describe, expect, it } from "vitest";
import { PageSizeControl } from "#/components/PageSizeControl";

describe(PageSizeControl, () => {
  const setPageSize = vi.fn();

  it("displays the current page size", () => {
    const result = render(<PageSizeControl pageSize={24} setPageSize={setPageSize} />);
    const button = result.getByRole("button");

    expect(button).toHaveTextContent("24 per page");
  });

  describe.each([12, 24, 60, 120, 240])("dropdown option for %i per page", (pageSize) => {
    const user = userEvent.setup();
    const renderDropdownOption = async (currentPageSize: number) => {
      const result = render(<PageSizeControl pageSize={currentPageSize} setPageSize={setPageSize} />);
      const button = result.getByRole("button");

      await user.click(button);
      const dropdownOptions = result.getAllByRole("button").slice(1);
      return dropdownOptions.filter((option) => option.textContent === `${pageSize} per page`).pop();
    };

    beforeEach(() => {
      setPageSize.mockReset();
    });

    it("is displayed in the dropdown", async () => {
      await expect(renderDropdownOption(pageSize)).resolves.toBeDefined();
    });

    it("is displayed as active when it is the current page size", async () => {
      await expect(renderDropdownOption(pageSize)).resolves.toHaveClass("active");
    });

    it("is not displayed as active when it is not the current page size", async () => {
      await expect(renderDropdownOption(1)).resolves.not.toHaveClass("active");
    });

    it("calls the setPageSize callback when clicked", async () => {
      const dropdownOption = await renderDropdownOption(1);

      expect(dropdownOption).toBeInTheDocument();

      await user.click(dropdownOption!);

      expect(setPageSize).toHaveBeenCalledExactlyOnceWith(pageSize);
    });
  });
});
