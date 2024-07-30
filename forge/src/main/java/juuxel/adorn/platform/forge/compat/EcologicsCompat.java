package juuxel.adorn.platform.forge.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class EcologicsCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "ecologics";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "azalea",
            "flowering_azalea",
            "coconut",
            "walnut"
        );
    }
}
