package juuxel.adorn.block.variant;

import java.util.List;

public final class MinecraftBlockVariants implements BlockVariantSet {
    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(
            BlockVariant.OAK,
            BlockVariant.SPRUCE,
            BlockVariant.BIRCH,
            BlockVariant.JUNGLE,
            BlockVariant.ACACIA,
            BlockVariant.DARK_OAK,
            BlockVariant.MANGROVE,
            BlockVariant.CHERRY,
            BlockVariant.BAMBOO,
            BlockVariant.CRIMSON,
            BlockVariant.WARPED
        );
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return List.of(
            BlockVariant.STONE,
            BlockVariant.COBBLESTONE,
            BlockVariant.SANDSTONE,
            BlockVariant.DIORITE,
            BlockVariant.ANDESITE,
            BlockVariant.GRANITE,
            BlockVariant.BRICK,
            BlockVariant.STONE_BRICK,
            BlockVariant.RED_SANDSTONE,
            BlockVariant.NETHER_BRICK,
            BlockVariant.BASALT,
            BlockVariant.BLACKSTONE,
            BlockVariant.RED_NETHER_BRICK,
            BlockVariant.PRISMARINE,
            BlockVariant.QUARTZ,
            BlockVariant.END_STONE_BRICK,
            BlockVariant.PURPUR,
            BlockVariant.POLISHED_BLACKSTONE,
            BlockVariant.POLISHED_BLACKSTONE_BRICK,
            BlockVariant.POLISHED_DIORITE,
            BlockVariant.POLISHED_ANDESITE,
            BlockVariant.POLISHED_GRANITE,
            BlockVariant.PRISMARINE_BRICK,
            BlockVariant.DARK_PRISMARINE,
            BlockVariant.CUT_SANDSTONE,
            BlockVariant.SMOOTH_SANDSTONE,
            BlockVariant.CUT_RED_SANDSTONE,
            BlockVariant.SMOOTH_RED_SANDSTONE,
            BlockVariant.SMOOTH_STONE,
            BlockVariant.MOSSY_COBBLESTONE,
            BlockVariant.MOSSY_STONE_BRICK,
            BlockVariant.DEEPSLATE,
            BlockVariant.COBBLED_DEEPSLATE,
            BlockVariant.POLISHED_DEEPSLATE,
            BlockVariant.DEEPSLATE_BRICK,
            BlockVariant.DEEPSLATE_TILE
        );
    }

    @Override
    public void addVariants(CustomVariantConsumer consumer) {
        consumer.add(BlockVariant.IRON, List.of(BlockKind.SHELF));
    }

    @Override
    public void sortVariants(Sorter sorter) {
        sorter.moveAfter(BlockVariant.IRON, getWoodVariants().getLast());
    }
}
