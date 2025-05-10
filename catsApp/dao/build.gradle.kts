plugins {
    id("java")
}

group = "Zmxcrr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service:models:"))
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