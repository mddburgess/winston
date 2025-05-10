import DOMPurify from "dompurify";

type HtmlTextProps = {
    text: string;
};

export const HtmlText = ({ text }: HtmlTextProps) => (
    <span dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(text) }} />
);
