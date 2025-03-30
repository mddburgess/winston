export const ascBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(a) - property(b);
}

export const descBy = <T>(property: (obj: T) => number) => {
    return (a: T, b: T) => property(b) - property(a);
}

export const pluralize = (count: number, single: string, plural: string = `${single}s`) => {
    return `${count} ` + (count === 1 ? single : plural)
}
