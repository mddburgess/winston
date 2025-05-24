import type { Maybe } from "#/types";

const ascBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(a) - property(b);
};

const descBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(b) - property(a);
};

const parseIntOrDefault = (value: Maybe<string>, defaultValue: number) => {
    if (!value) {
        return defaultValue;
    }
    const parsedValue = parseInt(value);
    return isNaN(parsedValue) ? defaultValue : parsedValue;
};

const pluralize = (
    count: number,
    single: string,
    plural: string = `${single}s`,
) => {
    return `${count} ` + (count === 1 ? single : plural);
};

const sumBy = <T>(items: T[], selector: (item: T) => number) => {
    return items.map(selector).reduce((acc, next) => acc + next, 0);
};

export { ascBy, descBy, parseIntOrDefault, pluralize, sumBy };
