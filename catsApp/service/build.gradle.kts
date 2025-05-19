plugins {
    id("java")
}

group = "Zmxcrr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":service:models")))
    implementation(project(mapOf("path" to ":dao")))
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