import { floor, isNaN } from "lodash";
import { ProgressBar } from "react-bootstrap";

type BasicProgressBarProps = {
  completed: number;
  total: number;
};

const BasicProgressBar = ({ completed, total }: BasicProgressBarProps) => (
  <ProgressBar
    animated={isNaN(total) || completed < total}
    variant={completed === total ? "success" : "primary"}
    now={total > 0 ? completed : 1}
    max={total > 0 ? total : 1}
    label={total > 0 ? `${floor((completed * 100) / total, 2)}%` : completed}
  />
);

export { BasicProgressBar };
