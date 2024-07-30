package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class PromenadeCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "promenade";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "dark_amaranth",
            "palm",
            "sakura"
        );
    }
}
