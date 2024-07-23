package juuxel.adorn.gradle.datagen;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

public abstract class DataGeneratorExtension {
    public abstract Property<Boolean> getGenerateTags();

    private final NamedDomainObjectContainer<DataConfig> configs;

    @Inject
    public DataGeneratorExtension(Project project) {
        configs = project.container(DataConfig.class);
        getGenerateTags().convention(false);
    }

    public NamedDomainObjectContainer<DataConfig> getConfigs() {
        return configs;
    }

    public void configs(Action<NamedDomainObjectContainer<DataConfig>> action) {
        action.execute(configs);
    }
}
