package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.Arrays;
import java.util.List;

public final class CinderscapesCompat implements BlockVariantSet {
    private static final String[] WOOD_VARIANTS = {
        "scorched",
        "umbral",
    };

    @Override
    public List<BlockVariant> getWoodVariants() {
        return Arrays.stream(WOOD_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Wood("cinderscapes/" + name))
            .toList();
    }
}
