plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ca.metricalsky.winston"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend"))
}

tasks {
    register<Copy>("copyFrontendResources") {
        dependsOn(project(":frontend").tasks.named("assemble"))
        from("${project(":frontend").projectDir}/dist")
        into("${project.buildDir}/resources/main/static")
    }

    processResources {
        dependsOn("copyFrontendResources")
    }

    bootJar {
        mainClass.set("ca.metricalsky.winston.Application")
    }

    bootRun {
        mainClass.set("ca.metricalsky.winston.Application")
        environment["SPRING_PROFILES_ACTIVE"] = "dev"
    }
}
