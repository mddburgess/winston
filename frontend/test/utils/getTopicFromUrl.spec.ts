import { getTopicFromUrl } from "#/utils/getTopicFromUrl";

describe(getTopicFromUrl, () => {
  it.each([
    { value: "https://en.wikipedia.org/wiki/Humour", expected: "Humour" },
    { value: "https://en.wikipedia.org/wiki/Lifestyle_(sociology)", expected: "Lifestyle (sociology)" },
  ])("gets topic '$expected' from URL '$value'", ({ value, expected }) => {
    expect(getTopicFromUrl(value)).toBe(expected);
  });
});
