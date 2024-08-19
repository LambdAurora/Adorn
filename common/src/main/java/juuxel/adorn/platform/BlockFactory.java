package juuxel.adorn.platform;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;

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
}
