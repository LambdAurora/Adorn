package juuxel.adorn.platform.forge.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public final class PaintedPlanksBlockNeo extends Block {
    public PaintedPlanksBlockNeo(Settings arg) {
        super(arg);
    }

    // getFireSpreadSpeed = burnChance
    // getFlammability = spreadChance

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFlammability(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return 20;
    }
}
