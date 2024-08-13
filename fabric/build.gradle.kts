plugins {
    id("adorn-data-generator")
    id("adorn-data-generator.emi")
    id("adorn-minify-json")
    id("adorn-service-inline")
}

// The files below are for using the access widener for the common project.
// We need to copy the file from common resources to fabric resource
// for Fabric Loader to find it and not crash.

// The path to the AW file in the common subproject.
val accessWidenerFile = project(":common").file("src/main/resources/adorn.accesswidener")

sourceSets {
    create("commonData") {
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
        compileClasspath += main.get().output
        runtimeClasspath += main.get().output
    }
}

loom {
    // Make the Fabric project use the common access widener.
    // Technically useless, BUT this file is also needed at dev runtime of course
    accessWidenerPath.set(accessWidenerFile)

    // Set up run config for data generator
    mods {
        register("data") {
            sourceSet("commonData")
        }
    }

    runs {
        register("commonData") {
            inherit(getByName("server"))
            configName = "Common Data Generator"
            source("commonData")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${project(":common").file("src/generated/resources")}")
            runDir("build/commonDataGenerator")
        }
    }
}

emiDataGenerator {
    setupForPlatform()
}

// Set up various Maven repositories for mod compat.
repositories {
    // Jitpack for Virtuoel's mods (Towelette compat).
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")

        // Since Jitpack is a very slow repository,
        // it's best to filter it to only include the artifacts we want.
        content {
            includeGroup("com.github.Virtuoel")
        }
    }

    // DashLoader maven.
    maven {
        url = uri("https://oskarstrom.net/maven")

        content {
            includeGroup("net.oskarstrom")
        }
    }
}

dependencies {
    // Depend on the common project. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }

    // Standard Fabric mod setup.
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    // Bundle Jankson in the mod and use it as a regular "implementation" library.
    implementation(libs.jankson)
    include(libs.jankson)

    // Mod compat
    modCompileOnly(libs.towelette)
    modCompileOnly(libs.modmenu)
    modLocalRuntime(libs.modmenu)
    modCompileOnly(libs.dashloader)
    modCompileOnly(libs.emi.fabric) {
        isTransitive = false
    }
}

tasks {
    processResources {
        // Mark that this task depends on the project version,
        // and should reset when the project version changes.
        inputs.property("version", project.version)

        // Replace the $version template in fabric.mod.json with the project version.
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}
