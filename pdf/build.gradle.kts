plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":Spec"))
    implementation("org.xhtmlrenderer:flying-saucer-core:9.9.5")
    implementation("org.xhtmlrenderer:flying-saucer-pdf-itext5:9.7.2")
    //implementation("org.slf4j:slf4j-nop:1.7.36")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}