package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.Arrays;
import java.util.List;

public final class BiomeMakeoverCompat implements BlockVariantSet {
    private static final String[] WOOD_VARIANTS = {
        "ancient_oak",
        "blighted_balsa",
        "willow",
        "swamp_cypress",
    };

    @Override
    public List<BlockVariant> getWoodVariants() {
        return Arrays.stream(WOOD_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Wood("biomemakeover/" + name))
            .toList();
    }
}
