package juuxel.adorn.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;

public abstract class VisibleBlockWithEntity extends BlockWithEntity {
    protected VisibleBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
