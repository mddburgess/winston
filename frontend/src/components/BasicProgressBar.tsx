import { round } from "lodash";
import { ProgressBar } from "react-bootstrap";

type BasicProgressBarProps = {
  completed: number;
  total: number;
};

const BasicProgressBar = ({ completed, total }: BasicProgressBarProps) => (
  <ProgressBar
    animated={completed < total}
    variant={completed === total ? "success" : "primary"}
    now={completed}
    max={total}
    label={`${round((completed * 100) / total)}%`}
  />
);

export { BasicProgressBar };
