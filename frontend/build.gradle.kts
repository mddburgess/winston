import com.github.gradle.node.npm.task.NpmTask

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
    }

    register<NpmTask>("format") {
        dependsOn(npmInstall)
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
