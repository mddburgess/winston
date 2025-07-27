plugins {
    java
    jacoco
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
    implementation(libs.bundles.springBoot)
    implementation(libs.bundles.youtube)
    implementation(libs.datasourceProxySpringBootStarter)
    implementation(libs.mapstruct)
    implementation(project(":api"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstructProcessor)
    annotationProcessor(libs.springBootConfigurationProcessor)

    runtimeOnly(libs.bundles.database)
    developmentOnly(libs.springBootDevtools)

    testImplementation(libs.archunitJunit5)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.wiremockSpringBoot)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks {
    compileJava {
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
