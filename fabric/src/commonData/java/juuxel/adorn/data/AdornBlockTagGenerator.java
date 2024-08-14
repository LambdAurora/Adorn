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
            .add(AdornBlocks.PLANKS.get().values().toArray(Block[]::new));
        getOrCreateTagBuilder(BlockTags.PLANKS).addTag(AdornTags.PAINTED_PLANKS.block());
    }
}
