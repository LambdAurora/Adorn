package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class CinderscapesCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "cinderscapes";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "scorched",
            "umbral"
        );
    }
}
