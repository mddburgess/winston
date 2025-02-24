plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ca.metricalsky.yt"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend"))
}

tasks {
    bootJar {
        mainClass.set("ca.metricalsky.yt.comments.Application")
    }

    bootRun {
        mainClass.set("ca.metricalsky.yt.comments.Application")
    }
}
