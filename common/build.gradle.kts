plugins {
    id("adorn-data-generator")
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    implementation(libs.jankson)

    // Just for @Environment and mixin deps :)
    modImplementation(libs.fabric.loader)

    // Add a mod dependency on some APIs for compat code.
    modCompileOnly(libs.rei.common)
    modCompileOnly(libs.emi.common)
    modCompileOnly(libs.jei.fabric)
    compileOnly(libs.rei.annotations)
}

dataGenerator {
    generateTags.set(true)

    configs.register("all") {
        rootProject.subprojects {
            files.from(fileTree("src/data"))
        }
        tagsOnly.set(true)
    }
}
