plugins {
    kotlin("jvm")
    `java-library`
    id("org.jetbrains.dokka") version "1.8.10"//dodato
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnitPlatform()
}
tasks.dokkaJavadoc {
    outputDirectory.set(file("build/dokka/javadoc")) // Set the output directory
}//dodato