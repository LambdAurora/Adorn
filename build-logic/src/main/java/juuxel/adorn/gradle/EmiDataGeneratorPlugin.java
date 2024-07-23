package juuxel.adorn.gradle;

import juuxel.adorn.gradle.datagen.EmiDataGeneratorExtension;
import juuxel.adorn.gradle.datagen.GenerateData;
import juuxel.adorn.gradle.datagen.GenerateEmi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public final class EmiDataGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("emiDataGenerator", EmiDataGeneratorExtension.class);
        var generateEmi = project.getTasks().register("generateEmi", GenerateEmi.class, task -> {
            var generateMainData = project.getTasks().named("generateMainData", GenerateData.class);
            task.mustRunAfter(generateMainData);
            task.getOutput().convention(generateMainData.flatMap(GenerateData::getOutput));
        });
        project.getTasks().named("generateData", task -> task.dependsOn(generateEmi));
    }
}
