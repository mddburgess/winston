import { floor } from "lodash";

const prefixes = ["", "k", "M", "B", "T"];

const formatNumber = (value: number): string => {
  if (value < 1000) {
    return `${value}`;
  }

  const magnitude = floor(Math.log10(value));
  const order = Math.min(floor(magnitude / 3), prefixes.length - 1);
  const formattedValue = floor(value, 2 - magnitude) / Math.pow(10, order * 3);

  return `${formattedValue}${prefixes[order]}`;
};

export { formatNumber };
