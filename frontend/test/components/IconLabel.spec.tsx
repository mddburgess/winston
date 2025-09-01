import { render } from "@testing-library/react";
import { Star } from "react-bootstrap-icons";
import { IconLabel } from "#/components/IconLabel";

describe(IconLabel, () => {
  it("displays the icon", () => {
    const result = render(<IconLabel icon={Star} label={""} />);
    const icon = result.getByTestId("icon");

    expect(icon).toBeInTheDocument();
    expect(icon).toHaveClass("bi-star");
  });

  it("displays the label property if present", () => {
    const label = "label text";
    const result = render(<IconLabel icon={Star} label={label} />);
    const labelCol = result.getByTestId("label");

    expect(labelCol).toBeInTheDocument();
    expect(labelCol).toHaveTextContent(label);
  });

  it("displays the children if present", () => {
    const child = <span data-testid={"child"}>label</span>;
    const result = render(<IconLabel icon={Star}>{child}</IconLabel>);
    const label = result.getByTestId("child");

    expect(label).toBeInTheDocument();
  });
});
