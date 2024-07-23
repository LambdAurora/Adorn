package juuxel.adorn.gradle.datagen;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DataOutputImpl;
import juuxel.adorn.datagen.tag.TagGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GenerateData extends DefaultTask {
    @Input
    public abstract SetProperty<DataConfig> getConfigs();

    @Input
    public abstract Property<Boolean> getGenerateTags();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();

    @TaskAction
    public void generate() {
        var output = DataOutputImpl.load(getOutput().get().getAsFile().toPath());
        Function<DataConfig, Stream<Path>> configFileGetter = config -> config.getFiles().getFiles().stream().map(File::toPath);

        DataGenerator.generate(
            getConfigs().get().stream()
                .filter(config -> !config.getTagsOnly().get())
                .flatMap(configFileGetter)
                .toList(),
            output
        );

        if (getGenerateTags().get()) {
            TagGenerator.generateFromConfigFiles(getConfigs().get().stream().flatMap(configFileGetter).toList(), output);
        }

        output.finish();
    }
}
