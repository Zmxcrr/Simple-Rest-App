plugins {
    id("java")
}

group = "Zmxcrr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation(project(":service:models"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName("jar") {
    enabled = true
}