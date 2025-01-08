plugins {
    kotlin("jvm")
    application//dodato
    id("com.github.johnrengelman.shadow") version "7.1.2"//dodato
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":Spec"))
    runtimeOnly(project(":pt"))
    runtimeOnly(project(":csv"))
    runtimeOnly(project(":pdf"))
    runtimeOnly(project(":excel"))
}
application {
    mainClass.set("MainKt")
}//dodato
tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    mergeServiceFiles() // include meta-inf services files
}//dodato
tasks.test {
    useJUnitPlatform()
}