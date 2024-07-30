package juuxel.adorn.block.variant;

import java.util.List;

public interface BlockVariantSet {
    default List<BlockVariant> getWoodVariants() {
        return List.of();
    }

    default List<BlockVariant> getStoneVariants() {
        return List.of();
    }

    default void addVariants(CustomVariantConsumer consumer) {
        // No customised variants by default.
    }

    default void sortVariants(Sorter sorter) {
        // No custom sorting by default.
    }

    @FunctionalInterface
    interface CustomVariantConsumer {
        void add(BlockVariant variant, List<BlockKind> kinds);
    }

    @FunctionalInterface
    interface Sorter {
        void moveAfter(BlockVariant variant, BlockVariant after);
    }
}
