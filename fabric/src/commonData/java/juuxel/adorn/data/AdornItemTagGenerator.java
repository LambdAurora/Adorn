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
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
    }

    private void copy(AdornTags.TagPair tagPair) {
        copy(tagPair.block(), tagPair.item());
    }
}
