plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "Zmxcrr"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
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
    implementation(project(":dao:"))
    implementation(project(":service:"))
    implementation(project(":service:models"))
    implementation(project(":controller:"))

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies{
        // Spring Boot
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-security")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")

        // lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // Persistence
        runtimeOnly("com.h2database:h2")
        runtimeOnly("org.postgresql:postgresql")
        implementation("org.flywaydb:flyway-core")
        runtimeOnly("org.flywaydb:flyway-database-postgresql:10.11.0")

        testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}