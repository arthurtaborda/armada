import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

group = "net.artcoder"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "net.artcoder.armada.ArmadaApp"
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

javafx {
    version = "11"
    modules("javafx.controls")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    implementation("org.apache.logging.log4j:log4j-core:2.8.2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.8.2")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:1.1.1")
    testImplementation("org.awaitility:awaitility:3.0.0")
}
