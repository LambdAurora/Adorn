package juuxel.adorn.gradle.action;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import org.gradle.api.Action;
import org.gradle.api.Task;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * A simple action that you can add to a {@code Jar} task
 * that minifies all JSON files using {@link JsonSlurper} and {@link JsonOutput}.
 */
public final class MinifyJson implements Action<Task> {
    @Override
    public void execute(Task task) {
        var jar = task.getOutputs().getFiles().getSingleFile().toPath();
        try (var fs = FileSystems.newFileSystem(URI.create("jar:" + jar.toUri()), Map.of("create", false))) {
            for (Path root : fs.getRootDirectories()) {
                try (var jsons = Files.walk(root).filter(it -> Files.isRegularFile(it) && it.toString().endsWith(".json"))) {
                    var iter = jsons.iterator();
                    while (iter.hasNext()) {
                        var json = iter.next();
                        Files.writeString(json, JsonOutput.toJson(new JsonSlurper().parse(json)));
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
