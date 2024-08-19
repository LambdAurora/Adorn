package juuxel.adorn.platform.forge;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.platform.BlockFactory;
import juuxel.adorn.platform.forge.block.PaintedPlanksBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodSlabBlockNeo;
import juuxel.adorn.platform.forge.block.SofaBlockForge;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

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
}
