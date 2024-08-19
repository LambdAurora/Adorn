package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockLootTableGenerator extends FabricBlockLootTableProvider {
    public AdornBlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        AdornBlocks.PAINTED_PLANKS.get().values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_SLABS.get().values().forEach(this::slabDrops);
        AdornBlocks.PAINTED_WOOD_STAIRS.get().values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCES.get().values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.get().values().forEach(this::addDrop);
    }
}
