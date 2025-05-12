import { render } from "@testing-library/react";
import { HtmlText } from "./HtmlText";

describe(HtmlText, () => {
    it("renders HTML from a text string", () => {
        const text = "<b>bold</b>";

        const result = render(<HtmlText data-testid="htmlText" text={text} />);

        expect(result.getByTestId("htmlText")).toContainHTML(
            `<span data-testid="htmlText">${text}</span>`,
        );
    });

    it("sanitizes the text string", () => {
        const text = "<b>bo<script>alert(1)</script>ld</b>";

        const result = render(<HtmlText data-testid="htmlText" text={text} />);

        expect(result.getByTestId("htmlText")).toContainHTML(
            `<span data-testid="htmlText"><b>bold</b></span>`,
        );
    });
});
