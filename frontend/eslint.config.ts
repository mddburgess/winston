import javascript from "@eslint/js";
import vitestPlugin from "@vitest/eslint-plugin";
import importPlugin from "eslint-plugin-import";
import reactPlugin from "eslint-plugin-react";
import globals from "globals";
import typescript from "typescript-eslint";
import viteConfig from "./vite.config";

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
            "import/extensions": "warn",
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
                        "internal",
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
        settings: {
            "import/internal-regex": "^#/",
            "import/resolver": {
                vite: {
                    viteConfig,
                },
            },
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
        files: ["**/*.{spec,test}.{ts,tsx}"],
        rules: {
            "vitest/consistent-test-filename": [
                "warn",
                { pattern: ".*\\.spec\\.tsx?$" },
            ],
            "vitest/consistent-test-it": "warn",
            "vitest/max-expects": ["warn", { max: 5 }],
            "vitest/max-nested-describe": ["warn", { max: 3 }],
            "vitest/no-alias-methods": "warn",
            "vitest/no-conditional-expect": "error",
            "vitest/no-conditional-in-test": "error",
            "vitest/no-conditional-tests": "error",
            "vitest/no-disabled-tests": "error",
            "vitest/no-duplicate-hooks": "error",
            "vitest/no-focused-tests": "warn",
            "vitest/no-interpolation-in-snapshots": "warn",
            "vitest/no-standalone-expect": "error",
            "vitest/no-test-return-statement": "error",
            "vitest/padding-around-all": "warn",
            "vitest/prefer-comparison-matcher": "warn",
            "vitest/prefer-describe-function-title": "error",
            "vitest/prefer-each": "warn",
            "vitest/prefer-equality-matcher": "warn",
            "vitest/prefer-expect-resolves": "warn",
            "vitest/prefer-hooks-in-order": "warn",
            "vitest/prefer-hooks-on-top": "warn",
            "vitest/prefer-lowercase-title": "warn",
            "vitest/prefer-mock-promise-shorthand": "warn",
            "vitest/prefer-spy-on": "warn",
            "vitest/prefer-strict-boolean-matchers": "error",
            "vitest/prefer-strict-equal": "error",
            "vitest/prefer-to-be": "error",
            "vitest/prefer-to-be-object": "error",
            "vitest/prefer-to-contain": "error",
            "vitest/prefer-to-have-length": "error",
            "vitest/prefer-todo": "error",
            "vitest/prefer-vi-mocked": "error",
            "vitest/require-hook": "error",
            "vitest/require-to-throw-message": "error",
            "vitest/require-top-level-describe": "warn",
            "vitest/valid-expect-in-promise": "error",
        },
        settings: {
            vitest: {
                typecheck: true,
            },
        },
    },
);
