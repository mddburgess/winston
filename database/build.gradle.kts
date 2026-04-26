plugins {
    kotlin("jvm") version libs.versions.kotlin
}

group = rootProject.group
version = rootProject.version

kotlin {
    jvmToolchain(23)
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(libs.bundles.database)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
