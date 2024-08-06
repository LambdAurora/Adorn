package juuxel.adorn.gradle;

import juuxel.adorn.gradle.action.MinifyJson;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public final class MinifyJsonPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getTasks().named("remapJar", task -> {
            task.doLast(new MinifyJson());
        });
    }
}
