import { setupServer } from "msw/node";
import { handlers } from "./handlers.ts";

export const backend = setupServer(...handlers);
