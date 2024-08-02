package juuxel.adorn.platform;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.variant.BlockVariant;

public interface BlockFactory {
    BlockFactory DEFAULT = new BlockFactory() {};

    default SofaBlock createSofa(BlockVariant variant) {
        return new SofaBlock(variant);
    }
}
