group = rootProject.group
version = rootProject.version

plugins {
    java
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
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

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.path)
    configFile.set("$projectDir/openapi-config.json")
    globalProperties.set(mapOf("apis" to "", "models" to ""))
}

tasks {
    compileJava {
        dependsOn(openApiGenerate)
    }
}
