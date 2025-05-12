import { ascBy, descBy, parseIntOrDefault, pluralize, sumBy } from "./index";

describe(ascBy, () => {
    it("sorts values in ascending order by the specified property", () => {
        const list = ["apple", "banana", "pear"];

        expect(list.sort(ascBy((str) => str.length))).toStrictEqual([
            "pear",
            "apple",
            "banana",
        ]);
    });
});

describe(descBy, () => {
    it("sorts values in descending order by the specified property", () => {
        const list = ["apple", "banana", "pear"];

        expect(list.sort(descBy((str) => str.length))).toStrictEqual([
            "banana",
            "apple",
            "pear",
        ]);
    });
});

describe(parseIntOrDefault, () => {
    it("returns the parsed value when it is a positive number", () => {
        expect(parseIntOrDefault("10", 1)).toBe(10);
    });

    it("returns the parsed value when it is a negative number", () => {
        expect(parseIntOrDefault("-10", 1)).toBe(-10);
    });

    it("returns the parsed value when it is 0", () => {
        expect(parseIntOrDefault("0", 1)).toBe(0);
    });

    it("returns the default value when the input value is not a number", () => {
        expect(parseIntOrDefault("string", 1)).toBe(1);
    });

    it("returns the default value when the input value is null", () => {
        expect(parseIntOrDefault(null, 1)).toBe(1);
    });

    it("returns the default value when the input value is undefined", () => {
        expect(parseIntOrDefault(undefined, 1)).toBe(1);
    });
});

describe(pluralize, () => {
    it("returns plural value when count is 0", () => {
        expect(pluralize(0, "channel")).toBe("0 channels");
    });

    it("returns singular value when count is 1", () => {
        expect(pluralize(1, "channel")).toBe("1 channel");
    });

    it("returns plural value when count is greater than 1", () => {
        expect(pluralize(2, "channel")).toBe("2 channels");
    });

    it("returns custom plural value when it is specified", () => {
        expect(pluralize(2, "reply", "replies")).toBe("2 replies");
    });
});

describe(sumBy, () => {
    type Item = {
        property: number;
    };

    it("returns the sum of a list of items using a property selector", () => {
        const items: Item[] = [
            { property: 1 },
            { property: 4 },
            { property: 9 },
        ];

        expect(sumBy(items, (item) => item.property)).toBe(14);
    });

    it("returns 0 for an empty list", () => {
        const items: Item[] = [];

        expect(sumBy(items, (item) => item.property)).toBe(0);
    });
});
