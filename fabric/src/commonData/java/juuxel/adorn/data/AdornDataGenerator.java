package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class AdornDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(AdornGeneralDataGenerator::new);
        pack.addProvider(AdornTagGenerator::new);
        pack.addProvider(AdornBlockLootTableGenerator::new);
        pack.addProvider(AdornModelGenerator::new);
        pack.addProvider(AdornRecipeGenerator::new);
    }

    @Override
    public String getEffectiveModId() {
        return AdornCommon.NAMESPACE;
    }
}
