import globals from "globals";
import javascript from "@eslint/js";
import typescript from "typescript-eslint";
import react from "eslint-plugin-react";

export default typescript.config(
    {
        files: ["**/*.{ts,tsx}"],
        languageOptions: {
            globals: globals.browser,
        },
    },
    {
        extends: [javascript.configs.recommended],
    },
    {
        extends: [typescript.configs.recommendedTypeChecked],
        languageOptions: {
            parserOptions: {
                projectService: true,
                tsconfigRootDir: import.meta.dirname,
            },
        },
        rules: {
            "@typescript-eslint/no-unnecessary-condition": "warn",
            "@typescript-eslint/no-unused-vars": "warn",
        },
    },
    {
        extends: [
            react.configs.flat.recommended,
            react.configs.flat["jsx-runtime"],
        ],
        settings: {
            react: {
                version: "detect",
            },
        },
    },
);
