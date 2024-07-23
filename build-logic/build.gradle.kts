plugins {
    // The Kotlin DSL plugin sets up Kotlin with the correct version
    // and lets me do .gradle.kts files as plugins.
    // See https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin-dsl_plugin
    `kotlin-dsl`
    id("org.jmailen.kotlinter") version "3.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm-tree:9.4")
    implementation("io.github.juuxel:adorn-data-generator")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
