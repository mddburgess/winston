import { Maybe } from "../types";

export const ascBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(a) - property(b);
};

export const descBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(b) - property(a);
};

export const parseIntOrDefault = (
    value: Maybe<string>,
    defaultValue: number,
) => {
    if (!value) {
        return defaultValue;
    }
    const parsedValue = parseInt(value);
    return isNaN(parsedValue) ? defaultValue : parsedValue;
};

export const pluralize = (
    count: number,
    single: string,
    plural: string = `${single}s`,
) => {
    return `${count} ` + (count === 1 ? single : plural);
};
