import { DropdownButton, DropdownItem } from "react-bootstrap";

type PageSizeControlProps = {
  pageSize: number;
  setPageSize: (pageSize: number) => void;
};

const PageSizeControl = ({ pageSize, setPageSize }: PageSizeControlProps) => {
  const dropdownItems = [12, 24, 60, 120, 240].map((size) => (
    <DropdownItem
      key={size}
      active={size === pageSize}
      onClick={() => {
        setPageSize(size);
      }}
    >
      {size} per page
    </DropdownItem>
  ));

  return (
    <DropdownButton variant={"outline-secondary"} title={`${pageSize} per page`}>
      {dropdownItems}
    </DropdownButton>
  );
};

export { PageSizeControl };
