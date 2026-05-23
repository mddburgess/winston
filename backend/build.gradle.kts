plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    jacoco
    `java-test-fixtures`
    alias(libs.plugins.powerAssert)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

group = rootProject.group
version = rootProject.version

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

kotlin {
    jvmToolchain(23)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.apacheCommons)
    implementation(libs.bundles.mapstruct)
    implementation(libs.bundles.springBoot)
    implementation(libs.bundles.youtube)
    implementation(libs.datasourceProxySpringBootStarter)
    implementation(libs.jsonPatch)
    implementation(project(":api"))
    implementation(project(":database"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.bundles.mapstructProcessor)
    annotationProcessor(libs.springBootConfigurationProcessor)

    developmentOnly(libs.springBootDevtools)

    testFixturesApi(testFixtures(project(":database")))

    testFixturesImplementation(libs.bundles.springBoot)
    testFixturesImplementation(libs.bundles.youtube)
    testFixturesImplementation(project(":api"))

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.archunitJunit5)
    testImplementation(libs.mapstructSpringTestExtensions)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.wiremockSpringBoot)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testRuntimeOnly(libs.junitPlatformLauncher)
}

powerAssert {
    functions = listOf("io.kotest.matchers.shouldBe")
}

tasks {
    compileJava {
        inputs.files(
            fileTree("src/main/java"),
            "lombok.config"
        )
        options.compilerArgs.addAll(listOf(
            "-Amapstruct.unmappedTargetPolicy=ERROR"
        ))
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        testLogging {
            events("FAILED", "SKIPPED")
        }
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}
