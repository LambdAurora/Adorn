package juuxel.adorn.gradle.datagen;

import org.gradle.api.Named;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

public abstract class DataConfig implements Named {
    public abstract ConfigurableFileCollection getFiles();
    public abstract Property<Boolean> getTagsOnly();

    @Inject
    public DataConfig() {
        getTagsOnly().convention(false);
    }
}
