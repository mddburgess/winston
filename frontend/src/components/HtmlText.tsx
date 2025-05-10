import DOMPurify from "dompurify";
import { HTMLAttributes } from "react";

type HtmlTextProps = Omit<
    HTMLAttributes<HTMLSpanElement>,
    "children" | "dangerouslySetInnerHTML"
> & {
    text: string;
};

export const HtmlText = ({ text, ...spanAttributes }: HtmlTextProps) => {
    return (
        <span
            {...spanAttributes}
            dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(text) }}
        />
    );
};
