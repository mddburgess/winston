import { formatInteger } from "#/utils/formatInteger";

describe(formatInteger, () => {
  it.each([
    { value: 1, expected: "1" },
    { value: 12, expected: "12" },
    { value: 123, expected: "123" },
    { value: 1234, expected: "1.23k" },
    { value: 12345, expected: "12.3k" },
    { value: 123456, expected: "123k" },
    { value: 1234567, expected: "1.23M" },
    { value: 12345678, expected: "12.3M" },
    { value: 123456789, expected: "123M" },
    { value: 1234567890, expected: "1.23B" },
    { value: 12345678901, expected: "12.3B" },
    { value: 123456789012, expected: "123B" },
    { value: 1234567890123, expected: "1.23T" },
    { value: 12345678901234, expected: "12.3T" },
    { value: 123456789012345, expected: "123T" },
    { value: 1234567890123456, expected: "1230T" },
  ])("formats $value as '$expected'", ({ value, expected }) => {
    expect(formatInteger(value)).toStrictEqual(expected);
  });
});
