import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

group = rootProject.group
version = rootProject.version

node {
    download.set(true)
    workDir.set(file("$rootDir/.gradle/nodejs"))
}

tasks {
    register<Delete>("clean") {
        delete("coverage")
        delete("dist")
    }

    register<NpxTask>("npmCheckUpdates") {
        command.set("npm-check-updates")
        args.add("-u")
    }

    npmInstall {
        dependsOn("npmCheckUpdates")
    }

    register<NpmTask>("assemble") {
        dependsOn("npmInstall")
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
        dependsOn("npmInstall")
        npmCommand.addAll("test")
    }

    register<DefaultTask>("check") {
        dependsOn("test")
    }

    register<DefaultTask>("build") {
        dependsOn("assemble", "check")
    }
}
