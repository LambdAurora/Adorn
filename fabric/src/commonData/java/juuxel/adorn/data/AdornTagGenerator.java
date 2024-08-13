package juuxel.adorn.data;

import juuxel.adorn.datagen.DataOutput;
import juuxel.adorn.datagen.tag.TagGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class AdornTagGenerator extends AdornCustomDataGenerator {
    private static final String TAG_CONFIG_DIRS_PROPERTY = "adorn.data.tagConfigDirs";

    public AdornTagGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void run(DataOutput output) {
        var configFiles = Arrays.stream(System.getProperty(TAG_CONFIG_DIRS_PROPERTY).split(","))
            .map(Path::of)
            .flatMap(dir -> {
                try {
                    return Files.list(dir);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })
            .filter(path -> path.getFileName().toString().endsWith(".xml"))
            .toList();
        TagGenerator.generateFromConfigFiles(configFiles, output);
    }

    @Override
    public String getName() {
        return "Tags";
    }
}
