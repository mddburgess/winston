import { sanitize } from "dompurify";

type HtmlTextProps = {
    text: string;
};

export const HtmlText = ({ text }: HtmlTextProps) => (
    <span dangerouslySetInnerHTML={{ __html: sanitize(text) }} />
);
