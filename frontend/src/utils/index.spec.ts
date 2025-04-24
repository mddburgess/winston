import { ascBy, descBy, pluralize } from "./index";

describe("ascBy()", () => {
    it("sorts values in ascending order by the specified property", () => {
        const list = ["apple", "banana", "pear"];
        expect(list.sort(ascBy((str) => str.length))).toEqual([
            "pear",
            "apple",
            "banana",
        ]);
    });
});

describe("descBy()", () => {
    it("sorts values in descending order by the specified property", () => {
        const list = ["apple", "banana", "pear"];
        expect(list.sort(descBy((str) => str.length))).toEqual([
            "banana",
            "apple",
            "pear",
        ]);
    });
});

describe("pluralize()", () => {
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
