import { OverlayTrigger, Tooltip, type TooltipProps } from "react-bootstrap";
import { DateTime } from "luxon";

type DateProps = {
    date: string;
};

export const Date = ({ date }: DateProps) => (
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
