package juuxel.adorn.data;

import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public final class AdornItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public AdornItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        copy(AdornTags.PAINTED_PLANKS);
        copy(AdornTags.PAINTED_WOOD_SLABS);
        copy(AdornTags.PAINTED_WOOD_STAIRS);
        copy(AdornTags.PAINTED_WOOD_FENCES);
        copy(AdornTags.PAINTED_WOOD_FENCE_GATES);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
    }

    private void copy(AdornTags.TagPair tagPair) {
        copy(tagPair.block(), tagPair.item());
    }
}
