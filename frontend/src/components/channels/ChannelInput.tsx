import { Form, InputGroup } from "react-bootstrap";
import type { ChangeEvent } from "react";

const handleRegex = /^@([A-Za-z0-9_\-.]{0,30})$/;
const urlRegex = /^(?:https?:\/\/)?(?:www\.|m\.)?youtube\.com\/@([A-Za-z0-9_\-.]{3,30})/;

const extractChannelHandle = (rawValue: string): string => {
  const handleMatch = rawValue.match(handleRegex);
  if (handleMatch) {
    return handleMatch[1];
  }

  const urlMatch = rawValue.match(urlRegex);
  if (urlMatch) {
    return urlMatch[1];
  }

  return rawValue;
};

const ChannelInput = (props: { value: string; onChange: (channelHandle: string) => void; disabled: boolean }) => {
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    props.onChange(extractChannelHandle(event.target.value).trim());
  };

  return (
    <InputGroup>
      <InputGroup.Text>@</InputGroup.Text>
      <Form.Control autoFocus={true} value={props.value} onChange={handleChange} disabled={props.disabled} />
    </InputGroup>
  );
};

export { ChannelInput };
