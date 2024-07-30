package juuxel.adorn.platform.forge.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class ArchitectsPaletteCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "architects_palette";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("architects_palette/twisted"));
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return createVariants(
            BlockVariant.Stone::new,
            "myonite",
            "myonite_brick",
            "mushy_myonite_brick"
        );
    }
}
