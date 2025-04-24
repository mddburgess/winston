import globals from "globals";
import javascript from "@eslint/js";
import typescript from "typescript-eslint";
import react from "eslint-plugin-react";
import { defineConfig } from "eslint/config";

export default defineConfig([
    {
        files: [
            "**/*.{ts,tsx}"
        ],
        languageOptions: {
            globals: globals.browser,
            parserOptions: {
                projectService: true,
                tsconfigRootDir: import.meta.dirname,
            }
        },
        settings: {
            react: {
                version: "detect",
            }
        }
    },

    javascript.configs.recommended,
    typescript.configs.recommendedTypeChecked,
    react.configs.flat.recommended,
    react.configs.flat["jsx-runtime"],
]);
