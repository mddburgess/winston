import "@testing-library/jest-dom";
import { backend } from "#/mocks/backend";

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
