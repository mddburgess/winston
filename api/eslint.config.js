import { defineConfig } from "eslint/config";
import ymlPlugin from "eslint-plugin-yml";

export default defineConfig({
  files: ["src/**/*.{yaml,yml}"],
  extends: [
    ymlPlugin.configs["flat/standard"],
    ymlPlugin.configs["flat/prettier"],
  ],
  rules: {
    "yml/file-extension": "error",
    "yml/require-string-key": "error",
  },
});
