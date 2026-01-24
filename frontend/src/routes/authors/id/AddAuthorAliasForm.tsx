import { useState } from "react";
import { Form, InputGroup } from "react-bootstrap";
import { PlusCircleFill } from "react-bootstrap-icons";
import { usePatchAuthorMutation } from "#/api";
import { IconButton } from "#/components/IconButton";
import type { AuthorProps } from "#/types";
import type { ChangeEvent, FormEvent } from "react";

const AddAuthorAliasForm = (props: AuthorProps) => {
  const [value, setValue] = useState("");
  const [patchAuthor] = usePatchAuthorMutation();

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.value.startsWith("@")) {
      setValue(event.target.value.slice(1));
    } else {
      setValue(event.target.value);
    }
  };

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    void patchAuthor({
      handle: props.author.handle,
      body: [{ op: "add", path: "/aliases/-", value: `@${value}` }],
    });
    setValue("");
  };

  return (
    <Form onSubmit={handleSubmit}>
      <InputGroup>
        <InputGroup.Text>@</InputGroup.Text>
        <Form.Control placeholder={"Add new alias..."} value={value} onChange={handleChange} />
        <IconButton icon={PlusCircleFill} type={"submit"} disabled={value.length === 0} />
      </InputGroup>
    </Form>
  );
};
export { AddAuthorAliasForm };
