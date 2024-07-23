package juuxel.adorn.gradle;

import juuxel.adorn.gradle.action.MinifyJson;
import juuxel.adorn.gradle.datagen.DataGeneratorExtension;
import juuxel.adorn.gradle.datagen.DeleteDuplicates;
import juuxel.adorn.gradle.datagen.GenerateData;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;

public final class DataGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        var java = project.getExtensions().getByType(JavaPluginExtension.class);
        var extension = project.getExtensions().create("dataGenerator", DataGeneratorExtension.class, project);
        extension.getConfigs().register("main", config -> {
            config.getFiles().from(project.fileTree("src/data").filter(file -> file.getName().endsWith(".xml")));
        });

        var generatedResources = project.getLayout().getProjectDirectory().dir("src/main/generatedResources");
        var generateMainData = project.getTasks().register("generateMainData", GenerateData.class, task -> {
            task.getConfigs().set(extension.getConfigs());
            task.getGenerateTags().set(extension.getGenerateTags());
            task.getOutput().convention(generatedResources);
        });
        var generateData = project.getTasks().register("generateData", task -> task.dependsOn(generateMainData));
        project.getTasks().named("processResources", task -> task.mustRunAfter(generateData));
        project.getTasks().register("deleteDuplicateResources", DeleteDuplicates.class, task -> {
            task.getGenerated().convention(generatedResources);
            task.getMain().convention(project.getLayout().dir(
                java.getSourceSets()
                    .named("main")
                    .map(main -> main.getResources().getSrcDirs().iterator().next())
            ));
        });
        java.getSourceSets().named("main", main -> {
            main.getResources().srcDir(generatedResources);
            main.getResources().exclude(".cache");
        });
        project.afterEvaluate(p -> {
            p.getTasks().named("remapJar", task -> task.doLast(new MinifyJson()));
        });
    }
}
