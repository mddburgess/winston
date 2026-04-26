plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    kotlin("plugin.jpa") version libs.versions.kotlin
    alias(libs.plugins.springDependencyManagement)
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

    testImplementation(kotlin("test"))
    testImplementation(libs.springBootStarterDataJpaTest)
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
