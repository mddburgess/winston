plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.api-client:google-api-client:2.7.2")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20250128-2.0.0")
    implementation("com.google.http-client:google-http-client-gson:1.46.1")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("com.github.gavlyukovskiy:datasource-proxy-spring-boot-starter:1.10.0")
    implementation("org.apache.commons:commons-collections4:4.5.0-M3")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:3.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
