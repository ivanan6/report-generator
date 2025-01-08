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
    implementation("org.apache.poi:poi:5.3.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")
    implementation("org.apache.xmlbeans:xmlbeans:5.0.2")



}

tasks.test {
    useJUnitPlatform()
}