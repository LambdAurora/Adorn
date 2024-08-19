package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public final class AdornModelGenerator extends FabricModelProvider {
    public AdornModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        AdornBlocks.PAINTED_PLANKS.get().forEach((color, planks) -> {
            generator.registerCubeAllModelTexturePool(planks)
                .slab(AdornBlocks.PAINTED_WOOD_SLABS.get().get(color))
                .stairs(AdornBlocks.PAINTED_WOOD_STAIRS.get().get(color))
                .fence(AdornBlocks.PAINTED_WOOD_FENCES.get().get(color))
                .fenceGate(AdornBlocks.PAINTED_WOOD_FENCE_GATES.get().get(color));
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
    }
}
