import { useState } from "react";
import { Button, Form, FormControl, InputGroup } from "react-bootstrap";
import { Search, XCircleFill } from "react-bootstrap-icons";
import { useSearchParams } from "react-router";
import type { ChangeEvent, FormEvent } from "react";

const SearchControl = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const initialValue = searchParams.get("s") ?? "";
  const [value, setValue] = useState(initialValue);

  const showSubmitButton = value === "" || value !== initialValue;

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setValue(event.target.value);
  };

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    setSearchParams((searchParams) => {
      searchParams.delete("i");
      if (value.trim() !== "") {
        searchParams.set("s", value);
      } else {
        searchParams.delete("s");
      }
      return searchParams;
    });
  };

  return (
    <Form onSubmit={handleSubmit}>
      <InputGroup>
        <FormControl placeholder="Search..." value={value} onChange={handleChange} />
        {showSubmitButton && (
          <Button type={"submit"} className={"align-items-center d-flex"} onClick={handleSubmit}>
            <Search />
          </Button>
        )}
        {!showSubmitButton && (
          <Button
            variant={"danger"}
            className={"align-items-center d-flex"}
            onClick={() => {
              setValue("");
            }}
          >
            <XCircleFill />
          </Button>
        )}
      </InputGroup>
    </Form>
  );
};

export { SearchControl };
