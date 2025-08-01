import { DateTime } from "luxon";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import type { TooltipProps } from "react-bootstrap";

type DateProps = {
    date: string;
};

const Date = ({ date }: DateProps) => (
    <OverlayTrigger
        delay={{ show: 500, hide: 200 }}
        overlay={<DateTooltip date={date} />}
    >
        <span>{DateTime.fromISO(date).toRelative()}</span>
    </OverlayTrigger>
);

const DateTooltip = ({ date, ...props }: DateProps & TooltipProps) => (
    <Tooltip {...props}>
        {DateTime.fromISO(date).toLocaleString(DateTime.DATETIME_MED)}
    </Tooltip>
);

export { Date };
