plugins {
    id("java")
    id("application")
}

group = "Zmxcrr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lab1-data"))
    implementation(project(":lab1-application"))
    implementation(project(":lab1-presentation"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}