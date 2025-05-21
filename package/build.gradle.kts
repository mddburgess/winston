plugins {
    java
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

group = rootProject.group
version = rootProject.version

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

    springBoot {
        buildInfo()
    }

    bootJar {
        mainClass.set("ca.metricalsky.winston.Application")
        archiveBaseName.set("winston")
    }

    bootRun {
        mainClass.set("ca.metricalsky.winston.Application")
        environment["SPRING_PROFILES_ACTIVE"] = "dev"
    }
}
