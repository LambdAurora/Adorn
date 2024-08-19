package juuxel.adorn.platform;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;

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
}
