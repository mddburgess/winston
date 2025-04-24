import { defineConfig } from "vitest/config";

export default defineConfig({
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
