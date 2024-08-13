package juuxel.adorn.data;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DataOutput;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.nio.file.Path;
import java.util.List;

public final class AdornGeneralDataGenerator extends AdornCustomDataGenerator {
    private static final String MAIN_CONFIG_PROPERTY = "adorn.data.mainConfig";

    public AdornGeneralDataGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void run(DataOutput output) {
        DataGenerator.generate(List.of(Path.of(System.getProperty(MAIN_CONFIG_PROPERTY))), output);
    }

    @Override
    public String getName() {
        return "General Data";
    }
}
