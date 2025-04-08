import {render} from "@testing-library/react";
import {userEvent} from "@testing-library/user-event";
import {CopyToClipboard} from "./CopyToClipboard";

describe("CopyToClipboard", () => {

    it("displays a clipboard icon", () => {
        const result = render(<CopyToClipboard text={"text to copy"}/>);

        const clipboard = result.getByTestId("clipboard");
        expect(clipboard).toBeInTheDocument();
        expect(clipboard).toHaveClass("bi-clipboard");
    });

    it("copies the text property to the clipboard when clicked", async () => {
        const result = render(<CopyToClipboard text={"text to copy"}/>);

        const clipboard = result.getByTestId("clipboard");
        expect(clipboard).toBeInTheDocument();

        await userEvent.setup().click(clipboard);

        const clipboardText = await navigator.clipboard.readText();
        expect(clipboardText).toBe("text to copy");
    });

    it("displays a filled clipboard icon with a checkmark when clicked", async () => {
        const result = render(<CopyToClipboard text={"text to copy"}/>);

        const clipboard = result.getByTestId("clipboard");
        expect(clipboard).toBeInTheDocument();

        await userEvent.setup().click(clipboard);

        const clipboardAfterClick = result.getByTestId("clipboard");
        expect(clipboardAfterClick).toBeInTheDocument();
        expect(clipboardAfterClick).toHaveClass("bi-clipboard-check-fill");
    });
})
