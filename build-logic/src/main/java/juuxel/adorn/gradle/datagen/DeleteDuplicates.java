package juuxel.adorn.gradle.datagen;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;

public abstract class DeleteDuplicates extends DefaultTask {
    @InputDirectory
    public abstract DirectoryProperty getGenerated();

    @OutputDirectory
    public abstract DirectoryProperty getMain();

    @TaskAction
    public void delete() throws IOException {
        var generatedRoot = getGenerated().get().getAsFile().toPath();
        try (var files = Files.walk(generatedRoot).filter(Files::isRegularFile)) {
            var iter = files.iterator();
            while (iter.hasNext()) {
                var file = iter.next();
                Files.deleteIfExists(getMain().get().getAsFile().toPath().resolve(generatedRoot.relativize(file)));
            }
        }
    }
}
