import { Badge } from "react-bootstrap";

type BadgeListProps = {
    values?: string[];
    transformer?: (value: string) => string;
};

const identity = (value: string) => value;

export const BadgeList = ({
    values = [],
    transformer = identity,
}: BadgeListProps) => {
    const badges = values.map(transformer).map((value) => (
        <Badge
            key={value}
            pill
            className={
                "bg-secondary-subtle text-secondary-emphasis border border-secondary-subtle me-2 mb-2"
            }
        >
            {value}
        </Badge>
    ));

    return <>{...badges}</>;
};
