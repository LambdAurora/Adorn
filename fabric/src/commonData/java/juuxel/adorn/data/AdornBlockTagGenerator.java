package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public AdornBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(AdornTags.PAINTED_PLANKS.block())
            .add(AdornBlocks.PAINTED_PLANKS.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_SLABS.block())
            .add(AdornBlocks.PAINTED_WOOD_SLABS.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_STAIRS.block())
            .add(AdornBlocks.PAINTED_WOOD_STAIRS.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_FENCES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCES.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_FENCE_GATES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCE_GATES.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(BlockTags.PLANKS).addTag(AdornTags.PAINTED_PLANKS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).addTag(AdornTags.PAINTED_WOOD_SLABS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).addTag(AdornTags.PAINTED_WOOD_STAIRS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).addTag(AdornTags.PAINTED_WOOD_FENCES.block());
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).addTag(AdornTags.PAINTED_WOOD_FENCE_GATES.block());
    }
}
