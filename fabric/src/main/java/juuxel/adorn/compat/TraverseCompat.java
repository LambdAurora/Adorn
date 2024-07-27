package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.List;

public final class TraverseCompat implements BlockVariantSet {
    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("traverse/fir"));
    }
}
