package juuxel.adorn.block.variant;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class CompatBlockVariantSet implements BlockVariantSet {
    protected abstract String getModId();

    protected List<BlockVariant> createVariants(Function<String, BlockVariant> factory, String... variants) {
        return Arrays.stream(variants).map(variant -> factory.apply(getModId() + '/' + variant)).toList();
    }
}
