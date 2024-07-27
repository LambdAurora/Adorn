package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.Arrays;
import java.util.List;

public final class TerrestriaCompat implements BlockVariantSet {
    private static final String[] WOOD_VARIANTS = {
        "cypress",
        "hemlock",
        "japanese_maple",
        "rainbow_eucalyptus",
        "redwood",
        "rubber",
        "sakura",
        "yucca_palm",
        "willow",
    };

    private static final String[] STONE_VARIANTS = {
        "basalt",
        "basalt_cobblestone",
        "smooth_basalt",
        "basalt_brick",
        "mossy_basalt_cobblestone",
        "mossy_basalt_brick",
    };

    @Override
    public List<BlockVariant> getWoodVariants() {
        return Arrays.stream(WOOD_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Wood("terrestria/" + name))
            .toList();
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return Arrays.stream(STONE_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Stone("terrestria/" + name))
            .toList();
    }
}
