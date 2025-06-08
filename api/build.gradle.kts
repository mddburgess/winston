import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask

group = rootProject.group
version = rootProject.version

plugins {
    java
    alias(libs.plugins.node)
    alias(libs.plugins.openapiGenerator)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jacksonDatabindNullable)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterWeb)
}

java {
    sourceSets {
        main {
            java.srcDir(layout.buildDirectory.dir("generated/src/main/java"))
        }
    }
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

node {
    download.set(true)
    version.set("23.11.0")
    workDir.set(file("$rootDir/.gradle/nodejs"))
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.path)
    configFile.set("$projectDir/openapi-config.json")
    globalProperties.set(mapOf("apis" to "", "models" to ""))
}

tasks {
    register<NpxTask>("npmCheckUpdates") {
        command.set("npm-check-updates")
        args.add("-u")
    }

    npmInstall {
        dependsOn("npmCheckUpdates")
    }

    register<NpmTask>("format") {
        dependsOn(npmInstall)
        inputs.dir(".")
        outputs.dir(".")
        npmCommand.set(listOf("run", "format"))
    }

    register<NpmTask>("lint") {
        dependsOn("format")
        npmCommand.set(listOf("run", "lint"))
    }

    register<NpmTask>("validate") {
        dependsOn("lint")
        npmCommand.set(listOf("run", "validate"))
    }

    named("openApiGenerate") {
        dependsOn("validate")
    }

    compileJava {
        dependsOn(openApiGenerate)
    }

    register<Copy>("copyResources") {
        dependsOn("format")
        from("$projectDir/src")
        into(layout.buildDirectory.dir("resources/main/static/spec"))
    }

    processResources {
        dependsOn("copyResources")
    }
}
