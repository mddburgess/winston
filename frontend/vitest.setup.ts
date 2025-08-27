import "@testing-library/jest-dom";
import { backend } from "./test/mocks/backend";

beforeAll(() => {
  backend.listen({
    onUnhandledRequest: "error",
  });
});

afterEach(() => {
  backend.resetHandlers();
});

afterAll(() => {
  backend.close();
});
