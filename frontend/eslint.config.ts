import javascript from "@eslint/js";
import vitestPlugin from "@vitest/eslint-plugin";
import importPlugin from "eslint-plugin-import";
import reactPlugin from "eslint-plugin-react";
import globals from "globals";
import typescript from "typescript-eslint";

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
            "@typescript-eslint/consistent-type-exports": "error",
            "@typescript-eslint/consistent-type-imports": "error",
            "@typescript-eslint/no-import-type-side-effects": "error",
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
            "import/first": "warn",
            "import/group-exports": "warn",
            "import/newline-after-import": [
                "warn",
                { count: 1, exactCount: true, considerComments: true },
            ],
            "import/no-amd": "error",
            "import/no-commonjs": "error",
            "import/no-default-export": "warn",
            "import/no-deprecated": "error",
            "import/no-empty-named-blocks": "error",
            "import/no-extraneous-dependencies": "error",
            "import/no-import-module-exports": "error",
            "import/no-mutable-exports": "error",
            "import/no-named-default": "warn",
            "import/no-nodejs-modules": "error",
            "import/no-self-import": "error",
            "import/no-useless-path-segments": "warn",
            "import/order": [
                "warn",
                {
                    alphabetize: {
                        order: "asc",
                        orderImportKind: "asc",
                        caseInsensitive: true,
                    },
                    groups: [
                        "builtin",
                        "external",
                        "parent",
                        "sibling",
                        "index",
                        "object",
                        "type",
                    ],
                    named: true,
                },
            ],
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
    {
        extends: [vitestPlugin.configs.recommended],
        files: ["**/*.spec.{ts,tsx}"],
    },
);
