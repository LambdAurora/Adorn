package juuxel.adorn.gradle.datagen;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSetContainer;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;

public abstract class EmiDataGeneratorExtension {
    @Inject
    protected abstract Project getProject();

    public void setupForPlatform() {
        setupForPlatform(null);
    }

    public void setupForPlatform(@Nullable File generatedResources) {
        var project = getProject();
        project.getTasks().named("generateEmi", GenerateEmi.class, task -> {
            var resourceDirs = new ArrayList<>(getSourceSets(project.project(":common")).getByName("main").getResources().getSrcDirs());
            resourceDirs.addAll(getSourceSets(project).getByName("main").getResources().getSrcDirs());

            for (File dir : resourceDirs) {
                // Ignore the AW resource dir
                if (dir.equals(generatedResources)) continue;

                task.getRecipes().from(project.fileTree(dir, tree -> {
                    tree.include("data/adorn/recipe/**");

                    // The unpacking recipes create "uncraftable" vanilla items like
                    // nether wart, so exclude them.
                    tree.exclude("data/adorn/recipe/crates/unpack/**");
                }));
            }
        });
    }

    private static SourceSetContainer getSourceSets(Project project) {
        return project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
    }
}
