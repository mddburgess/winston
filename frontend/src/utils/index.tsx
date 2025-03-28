export const pluralize = (count: number, single: string, plural: string = `${single}s`) => {
    return `${count} ` + (count === 1 ? single : plural)
}
