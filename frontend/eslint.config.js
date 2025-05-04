import globals from "globals";
import javascript from "@eslint/js";
import typescript from "typescript-eslint";
import importPlugin from "eslint-plugin-import";
import reactPlugin from "eslint-plugin-react";
import { defineConfig } from "eslint/config";

export default defineConfig([
    {
        files: ["**/*.{ts,tsx}"],
        languageOptions: {
            globals: globals.browser,
            parserOptions: {
                projectService: true,
                tsconfigRootDir: import.meta.dirname,
            },
        },
    },
    {
        extends: [javascript.configs.recommended],
    },
    {
        extends: [typescript.configs.recommendedTypeChecked],
        rules: {
            "@typescript-eslint/consistent-type-exports": "warn",
            "@typescript-eslint/consistent-type-imports": "warn",
            "@typescript-eslint/no-unnecessary-condition": "warn",
            "@typescript-eslint/no-unused-vars": "warn",
        },
    },
    {
        extends: [
            importPlugin.flatConfigs.recommended,
            importPlugin.flatConfigs.typescript,
        ],
        rules: {
            "import/consistent-type-specifier-style": "warn",
            "import/exports-last": "warn",
        },
    },
    {
        extends: [
            reactPlugin.configs.flat.recommended,
            reactPlugin.configs.flat["jsx-runtime"],
        ],
        settings: {
            react: {
                version: "detect",
            },
        },
    },
]);
