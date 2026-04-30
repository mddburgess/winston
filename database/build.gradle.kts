plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    kotlin("plugin.jpa") version libs.versions.kotlin
    alias(libs.plugins.kover)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    `java-test-fixtures`
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
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.kotlinReflect)

    runtimeOnly(libs.bundles.database)

    testFixturesApi(libs.bundles.kotest)
    testFixturesApi(libs.datafaker)
    testFixturesApi(libs.springBootStarterTest)

    testImplementation(kotlin("test"))
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(koverHtmlReport, koverXmlReport)
    }

    bootJar {
        enabled = false
    }
}
