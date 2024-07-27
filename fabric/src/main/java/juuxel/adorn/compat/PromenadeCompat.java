package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.Arrays;
import java.util.List;

public final class PromenadeCompat implements BlockVariantSet {
    private static final String[] WOOD_VARIANTS = {
        "dark_amaranth",
        "palm",
        "sakura",
    };

    @Override
    public List<BlockVariant> getWoodVariants() {
        return Arrays.stream(WOOD_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Wood("promenade/" + name))
            .toList();
    }
}
