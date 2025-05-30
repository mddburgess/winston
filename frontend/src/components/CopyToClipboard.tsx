import { useState } from "react";
import { Clipboard, ClipboardCheckFill } from "react-bootstrap-icons";

type CopyToClipboardProps = {
    text: string;
};

export const CopyToClipboard = ({ text }: CopyToClipboardProps) => {
    const [copied, setCopied] = useState(false);
    const handleOnClick = () => {
        void navigator.clipboard.writeText(text).then(() => setCopied(true));
    };
    const ClipboardIcon = copied ? ClipboardCheckFill : Clipboard;

    return (
        <ClipboardIcon
            className={"cursor-pointer ms-2"}
            data-testid={"clipboard"}
            onClick={handleOnClick}
        />
    );
};
