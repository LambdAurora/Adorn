package juuxel.adorn.platform;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WoodType;

public interface BlockFactory {
    BlockFactory DEFAULT = new BlockFactory() {};

    default SofaBlock createSofa(BlockVariant variant) {
        return new SofaBlock(variant);
    }

    default Block createPaintedPlanks(AbstractBlock.Settings settings) {
        return new Block(settings);
    }

    default Block createPaintedWoodSlab(AbstractBlock.Settings settings) {
        return new SlabBlock(settings);
    }

    default Block createPaintedWoodStairs(BlockState baseBlockState, AbstractBlock.Settings settings) {
        return new StairsBlock(baseBlockState, settings);
    }

    default Block createPaintedWoodFence(AbstractBlock.Settings settings) {
        return new FenceBlock(settings);
    }

    default Block createPaintedWoodFenceGate(WoodType woodType, AbstractBlock.Settings settings) {
        return new FenceGateBlock(woodType, settings);
    }
}
