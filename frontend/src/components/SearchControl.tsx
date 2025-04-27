import { Button, FormControl, InputGroup } from "react-bootstrap";
import { Search, XCircleFill } from "react-bootstrap-icons";
import { ChangeEvent } from "react";

type SearchControlProps = {
    value?: string;
    setValue?: (value: string) => void;
};

export const SearchControl = ({ value, setValue }: SearchControlProps) => {
    if (!setValue) {
        return <></>;
    }

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        event.preventDefault();
        setValue(event.target.value);
    };

    return (
        <InputGroup>
            <FormControl
                placeholder="Search..."
                value={value}
                onChange={handleChange}
            />
            {!value && (
                <Button className={"align-items-center d-flex"}>
                    <Search />
                </Button>
            )}
            {value && (
                <Button
                    variant={"danger"}
                    className={"align-items-center d-flex"}
                    onClick={() => setValue("")}
                >
                    <XCircleFill />
                </Button>
            )}
        </InputGroup>
    );
};
