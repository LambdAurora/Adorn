package juuxel.adorn.platform.forge.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WoodType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public final class PaintedWoodFenceGateBlockNeo extends FenceGateBlock {
    public PaintedWoodFenceGateBlockNeo(WoodType woodType, Settings settings) {
        super(woodType, settings);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return PaintedPlanksBlockNeo.BURN_CHANCE;
    }

    @Override
    public int getFlammability(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return PaintedPlanksBlockNeo.SPREAD_CHANCE;
    }
}
