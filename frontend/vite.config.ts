import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";
import * as path from "node:path";

export default defineConfig({
    build: {
        rollupOptions: {
            onwarn: (warning, warn) => {
                // react-bootstrap throws a lot of warnings
                if (warning.code !== "MODULE_LEVEL_DIRECTIVE") {
                    warn(warning);
                }
            },
        },
    },
    plugins: [react()],
    resolve: {
        alias: {
            "#": path.resolve(import.meta.dirname, "./src/"),
        },
    },
    server: {
        proxy: {
            "/api": {
                target: "http://localhost:8080/",
            },
        },
    },
    test: {
        coverage: {
            include: ["**/src/**/*.{ts,tsx}"],
            reporter: [
                "cobertura",
                ["html", { subdir: "report" }],
                "text-summary",
            ],
        },
        globals: true,
        reporters: [
            "default",
            [
                "junit",
                {
                    suiteName: "frontend tests",
                    outputFile: "coverage/junit.xml",
                },
            ],
        ],
        setupFiles: ["vitest.setup.ts"],
    },
});
