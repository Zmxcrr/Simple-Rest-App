plugins {
    id("java")
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
    implementation(project(":dao"))
    implementation(project(":service"))
    implementation(project(":service:models"))
    implementation(project(":controller"))

    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
}

allprojects {
    apply(plugin = "java")

    dependencies{
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        runtimeOnly("com.h2database:h2:2.2.224")
        runtimeOnly("org.postgresql:postgresql:42.7.1")

        implementation("org.flywaydb:flyway-core:10.11.0")
        runtimeOnly("org.flywaydb:flyway-database-postgresql:10.11.0")
        testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")

        testImplementation("org.mockito:mockito-core:5.3.1")
        testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}

tasks.test {
    useJUnitPlatform()

    filter {
        includeTestsMatching("*Test")
        includeTestsMatching("*Tests")
    }
}
