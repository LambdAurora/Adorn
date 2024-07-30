package juuxel.adorn.platform.forge;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.platform.BlockFactory;
import juuxel.adorn.platform.forge.block.SofaBlockForge;

public final class BlockFactoryImpl implements BlockFactory {
    public static final BlockFactoryImpl INSTANCE = new BlockFactoryImpl();

    @Override
    public SofaBlock createSofa(BlockVariant variant) {
        return new SofaBlockForge(variant);
    }
}
