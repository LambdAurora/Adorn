package juuxel.adorn.platform.forge;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.platform.BlockFactory;
import juuxel.adorn.platform.forge.block.PaintedPlanksBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodFenceBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodFenceGateBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodSlabBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodStairsBlockNeo;
import juuxel.adorn.platform.forge.block.SofaBlockForge;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;

public final class BlockFactoryImpl implements BlockFactory {
    public static final BlockFactoryImpl INSTANCE = new BlockFactoryImpl();

    @Override
    public SofaBlock createSofa(BlockVariant variant) {
        return new SofaBlockForge(variant);
    }

    @Override
    public Block createPaintedPlanks(AbstractBlock.Settings settings) {
        return new PaintedPlanksBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodSlab(AbstractBlock.Settings settings) {
        return new PaintedWoodSlabBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodStairs(BlockState baseBlockState, AbstractBlock.Settings settings) {
        return new PaintedWoodStairsBlockNeo(baseBlockState, settings);
    }

    @Override
    public Block createPaintedWoodFence(AbstractBlock.Settings settings) {
        return new PaintedWoodFenceBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodFenceGate(WoodType woodType, AbstractBlock.Settings settings) {
        return new PaintedWoodFenceGateBlockNeo(woodType, settings);
    }
}
