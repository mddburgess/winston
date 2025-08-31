import { floor } from "lodash";

const suffixes = ["", "k", "M", "B", "T"];

/**
 * Formats an integer as a string.
 *
 * Integers smaller than 1000 are returned as-is. For numbers greater than or equal to 1000, the number is formatted to
 * preserve the three most significant digits, and the suffix representing the nearest thousands grouping is added.
 *
 * For example, 12345 is formatted is "12.3k", and 123 million is formatted as "123M".
 *
 * @param value the integer to format
 * @returns the formatted number string
 */
const formatInteger = (value: number): string => {
  if (value < 1000) {
    return `${value}`;
  }

  const magnitude = floor(Math.log10(value));
  const order = Math.min(floor(magnitude / 3), suffixes.length - 1);
  const formattedValue = floor(value, 2 - magnitude) / Math.pow(10, order * 3);

  return `${formattedValue}${suffixes[order]}`;
};

export { formatInteger };
