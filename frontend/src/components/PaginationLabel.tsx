import { pluralize } from "#/utils";

type PaginationLabelProps = {
  itemLabel: string;
  pluralLabel?: string;
  itemCount: number;
  totalCount: number;
  pageSize: number;
  pageNumber: number;
};

const PaginationLabel = ({
  itemLabel,
  pluralLabel,
  itemCount,
  totalCount,
  pageSize,
  pageNumber,
}: PaginationLabelProps) => {
  const firstIndex = pageNumber * pageSize + 1;
  const lastIndex = firstIndex + itemCount - 1;

  const label =
    itemCount <= 0 || itemCount === totalCount
      ? pluralize(itemCount, itemLabel, pluralLabel)
      : `${firstIndex} – ${lastIndex} of ${pluralize(totalCount, itemLabel, pluralLabel)}`;

  return <span>{label}</span>;
};

export { PaginationLabel };
