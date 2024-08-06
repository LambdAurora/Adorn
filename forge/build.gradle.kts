plugins {
    id("adorn-data-generator")
    id("adorn-data-generator.emi")
    id("adorn-service-inline")
}

architectury {
    // Create the IDE launch configurations for this subproject.
    platformSetupLoomIde()
    // Set up Architectury for NeoForge.
    neoForge()
}

loom {
    // Make the Forge project use the common access widener.
    accessWidenerPath.set(project(":common").file("src/main/resources/adorn.accesswidener"))
}

emiDataGenerator {
    setupForPlatform()
}

repositories {
    // Set up NeoForge's Maven repository.
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    // Add dependency on NeoForge. This is mainly used for generating the patched Minecraft jar with NeoForge classes.
    neoForge(libs.neoforge)

    // Depend on the common project. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    // Bundle the transformed version of the common project in the mod.
    // The transformed version includes things like fixed refmaps.
    bundle(project(path = ":common", configuration = "transformProductionNeoForge")) {
        isTransitive = false
    }

    // Bundle Jankson in the mod.
    bundle(libs.jankson)
    // Use Jankson as a library. Note that on Forge, regular non-mod libraries have to be declared
    // using forgeRuntimeLibrary as Forge reads the runtime classpath from a separately generated file.
    // In ForgeGradle projects, you might see a custom "library" configuration used for this.
    forgeRuntimeLibrary(libs.jankson)

    // Add regular mod dependency on REI - API for compile time and the mod itself for runtime.
    // modLocalRuntime won't be exposed if other mods depend on your mod unlike modRuntimeOnly.
    modCompileOnly(libs.rei.neoforge)
}

tasks {
    shadowJar {
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    remapJar {
        // Convert the access widener to a NeoForge access transformer.
        atAccessWideners.add("adorn.accesswidener")
        archiveClassifier.set("neoforge")
    }

    processResources {
        // Mark that this task depends on the project version,
        // and should reset when the project version changes.
        inputs.property("version", project.version)

        // Replace the $version template in mods.toml with the project version.
        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}
