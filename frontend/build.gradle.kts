import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask

plugins {
    alias(libs.plugins.node)
}

group = rootProject.group
version = rootProject.version

node {
    download.set(true)
    version.set("23.11.0")
    workDir.set(file("$rootDir/.gradle/nodejs"))
}

tasks {
    register<Delete>("clean") {
        delete("coverage")
        delete("dist")
        delete("src/api")
    }

    register<NpxTask>("npmCheckUpdates") {
        command.set("npm-check-updates")
        args.add("-u")
    }

    npmInstall {
        dependsOn("npmCheckUpdates")
    }

    register<NpxTask>("codegenOpenApi") {
        dependsOn(npmInstall)
        inputs.files(
            project(":api").fileTree("src"),
            "openapi-config.js"
        )
        outputs.dir("src/api")
        command.set("@rtk-query/codegen-openapi")
        args.add("openapi-config.js")
    }

    register<NpmTask>("format") {
        dependsOn("codegenOpenApi")
        npmCommand.addAll("run", "format")
    }

    register<NpmTask>("compileTypescript") {
        dependsOn("format")
        npmCommand.addAll("run", "compile")
    }

    register<NpmTask>("assemble") {
        dependsOn("compileTypescript")
        inputs.files(
            fileTree("node_modules"),
            fileTree("src"),
            "package.json",
            "tsconfig.json"
        )
        outputs.dir("dist")
        npmCommand.addAll("run", "build")
    }

    register<NpmTask>("test") {
        dependsOn("compileTypescript")
        npmCommand.addAll("test")
    }

    register<DefaultTask>("check") {
        dependsOn("test")
    }

    register<DefaultTask>("build") {
        dependsOn("assemble", "check")
    }
}
